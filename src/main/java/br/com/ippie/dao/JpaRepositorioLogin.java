package br.com.ippie.dao;

import br.com.ippie.negocio.Login;
import br.com.ippie.negocio.RepositorioDeLogins;
import br.com.ippie.negocio.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioLogin implements RepositorioDeLogins 
{
@PersistenceContext
private EntityManager manager;
private final AuxiliarJpa jpa;

    public JpaRepositorioLogin()
    {
    jpa=new AuxiliarJpa();
    }
    
    @Override
    public Login existeUsuario(String email) 
    {
    return jpa.login(manager.createNativeQuery("select cd_login,nm_email,"
            + "nm_senha from login where nm_email=:email")
            .setParameter("email",email).getResultList());
    }

    @Override
    public Usuario existeUsuario(Login l) 
    {
    Usuario u=(jpa.usuarios(manager.createNativeQuery("select cd_usuario,nm_nome,"
            + "nm_sobrenome,nm_foto_perfil,nm_foto_cabecalho,nm_descricao,"
            + "login.cd_login,nm_senha,ic_moderador,nm_email from usuario inner join "
            + "login on usuario.cd_login=login.cd_login where nm_email=:email "
            + "and ic_ativo=true").setParameter("email",l.getEmail())
            .getResultList()));
      if(u==null)
      {
      return u;
      }
      if(l.senhasSaoIguais(u.getLogin().getSenha()))
      {
      u.setAssuntosFavoritos(jpa.assuntos(manager.createNativeQuery("select "
              + "assunto.cd_assunto,nm_assunto,nm_url_assunto,dt_assunto from assunto "
              + "inner join item_usuario_assunto on "
              + "assunto.cd_assunto=item_usuario_assunto.cd_assunto where "
              + "cd_usuario=:usuario").setParameter("usuario",u.getCodigo())
              .getResultList()));
      return u;
      }
      if(u.getLogin().getSenha().equals(""))
      {
      manager.createNativeQuery("update login set nm_senha=:senha where cd_login=:login")
              .setParameter("senha",l.geraSenhaCriptografada())
              .setParameter("login",u.getLogin().getCodigo()).executeUpdate();
      return u;
      }
    return null;
    }

    @Override
    public void editaSenha(Login l) 
    {
    manager.createNativeQuery("update login set nm_senha=:senha where "
            + "nm_email=:email").setParameter("senha",l.geraSenhaCriptografada())
            .setParameter("email",l.getEmail()).executeUpdate();
    }
}