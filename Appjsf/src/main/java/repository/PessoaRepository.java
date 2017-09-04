package repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import entity.PessoaEntity;
import entity.UsuarioEntity;
import model.PessoaModel;
import model.UsuarioModel;
import uteis.Uteis;

public class PessoaRepository {

	@Inject
	PessoaEntity pessoaEntity;
	
	EntityManager entityManager;
	
	public void salvarNovoRegistro(PessoaModel pessoaModel) {
		
		entityManager = Uteis.JpaEntityManager();
		
		pessoaEntity = new PessoaEntity();
		pessoaEntity.setDataCadastro(LocalDateTime.now());
		pessoaEntity.setEmail(pessoaModel.getEmail());
		pessoaEntity.setEndereco(pessoaModel.getEndereco());
		pessoaEntity.setNome(pessoaModel.getNome());
		pessoaEntity.setOrigemCadastro(pessoaModel.getOrigemCadastro());
		pessoaEntity.setSexo(pessoaModel.getSexo());
		
		UsuarioEntity usuarioEntity = entityManager.find(UsuarioEntity.class, pessoaModel.getUsuarioModel().getCodigo());
		
		pessoaEntity.setUsuarioEntity(usuarioEntity);
		
		entityManager.persist(pessoaEntity);
	}
	
	public List<PessoaModel> getPessoas() {
		
		List<PessoaModel> pessoasModel = new ArrayList<PessoaModel>();
		
		entityManager = Uteis.JpaEntityManager();
		
		Query query = entityManager.createNamedQuery("PessoaEntity.findAll");
		
		@SuppressWarnings("unchecked")
		Collection<PessoaEntity> pessoasEntity = (Collection<PessoaEntity>) query.getResultList();
		
		PessoaModel pessoaModel = null;
		
		for (PessoaEntity pessoaEntity : pessoasEntity) {
			pessoaModel = new PessoaModel();
			pessoaModel.setCodigo(pessoaEntity.getCodigo());
			pessoaModel.setEmail(pessoaEntity.getEmail());
			pessoaModel.setEndereco(pessoaEntity.getEndereco());
			pessoaModel.setDataCadastro(pessoaEntity.getDataCadastro());
			pessoaModel.setNome(pessoaEntity.getNome());
			
			if (pessoaEntity.getOrigemCadastro().equals("X")) {
				pessoaModel.setOrigemCadastro("XML");
			} else {
				pessoaModel.setOrigemCadastro("INPUT");
			}
			
			if (pessoaEntity.getSexo().equals("M")) {
				pessoaModel.setSexo("Masculino");
			} else {
				pessoaModel.setSexo("Feminino");
			}
			
			UsuarioEntity usuarioEntity = pessoaEntity.getUsuarioEntity();
			
			UsuarioModel usuarioModel = new UsuarioModel();
			usuarioModel.setUsuario(usuarioEntity.getUsuario());
			
			pessoaModel.setUsuarioModel(usuarioModel);
			
			pessoasModel.add(pessoaModel);
		}
		
		return pessoasModel;
	}
}
