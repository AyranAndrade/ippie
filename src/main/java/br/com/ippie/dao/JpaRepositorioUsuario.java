package br.com.ippie.dao;

import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeUsuarios;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Usuario;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioUsuario implements RepositorioDeUsuarios 
{
@PersistenceContext
private EntityManager manager;
private final AuxiliarJpa jpa;

    public JpaRepositorioUsuario() 
    {
    jpa=new AuxiliarJpa();
    }

    @Override
    public Collection<Usuario> busca(String nome) 
    {
    return jpa.usuariosParaPesquisa(manager.createNativeQuery("select cd_usuario,nm_nome,"
            + "nm_sobrenome,nm_foto_perfil,nm_descricao,nm_foto_cabecalho from "
            + "usuario where replace(lower(concat(nm_nome,nm_sobrenome)),' ','')"
            + " like :nome limit 5").setParameter("nome",nome.trim()
                    .toLowerCase()+"%").getResultList());
    }

    @Override
    public void adiciona(Usuario u)
    {
    manager.persist(u);
    }

    @Override
    public void exclui(Usuario u)
    {
    manager.remove(u);
    }

    @Override
    public void editaSocial(Usuario u) 
    {
    manager.createNativeQuery("update usuario set nm_nome=:nome,"
            + "nm_sobrenome=:sobrenome,nm_descricao=:descricao where "
            + "cd_usuario=:usuario").setParameter("nome",u.getNome())
            .setParameter("sobrenome",u.getSobrenome())
            .setParameter("descricao",u.getDescricao())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void desbloqueia(Usuario u, Assunto t) 
    {
    manager.createNativeQuery("delete from item_assunto_bloqueado where "
            + "cd_usuario=:usuario and cd_assunto=:tag")
            .setParameter("usuario",u.getCodigo())
            .setParameter("tag",t.getCodigo()).executeUpdate();
    }

    @Override
    public void editaAssuntosFavoritos(Usuario u)
    {
    manager.createNativeQuery("delete from item_usuario_assunto where "
            + "cd_usuario=:usuario").setParameter("usuario",u.getCodigo())
            .executeUpdate();
      u.getAssuntosFavoritos().stream().forEach((t)->{
      manager.createNativeQuery("insert into item_usuario_assunto "
              + "values(:usuario,:tag)").setParameter("usuario",u.getCodigo())
              .setParameter("tag",t.getCodigo()).executeUpdate();
      });
    }

    @Override
    public void editaDescricao(Usuario u) 
    {
    manager.createNativeQuery("update usuario set nm_descricao=:descricao where"
            + " cd_usuario=:usuario")
            .setParameter("descricao",u.getDescricao())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void editaFotoCabecalho(Usuario u)
    {
    manager.createNativeQuery("update usuario set nm_foto_cabecalho=:capa where "
            + "cd_usuario=:codigo").setParameter("capa",u.getFotoCabecalho())
            .setParameter("codigo",u.getCodigo()).executeUpdate();
    }

    @Override
    public void editaFotoPerfil(Usuario u)
    {
    manager.createNativeQuery("update usuario set nm_foto_perfil=:perfil where "
            + "cd_usuario=:codigo").setParameter("perfil",u.getFotoPerfil())
            .setParameter("codigo",u.getCodigo()).executeUpdate();
    }

    @Override
    public void bloqueia(Usuario bloqueador, Usuario bloqueado) 
    {
    manager.createNativeQuery("insert into item_usuario_bloqueado "
            + "values(:bloqueador,:bloqueado,current_timestamp())")
            .setParameter("bloqueador",bloqueador.getCodigo())
            .setParameter("bloqueado",bloqueado.getCodigo()).executeUpdate();
    }

    @Override
    public void bloqueia(Usuario u, Conteudo p)
    {
    manager.createNativeQuery("insert into item_conteudo_bloqueado "
            + "values(:post,:usuario,current_timestamp())")
            .setParameter("post",p.getCodigo())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void bloqueia(Usuario u, Assunto t) 
    {
    manager.createNativeQuery("insert into item_assunto_bloqueado "
            + "values(:usuario,:tag,current_timestamp())")
            .setParameter("tag",t.getCodigo())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void desbloqueia(Usuario bloqueador, Usuario bloqueado) 
    {
    manager.createNativeQuery("delete from item_usuario_bloqueado where "
            + "cd_bloqueador=:bloqueador and cd_bloqueado=:bloqueado")
            .setParameter("bloqueador",bloqueador.getCodigo())
            .setParameter("bloqueado",bloqueado.getCodigo()).executeUpdate();
    }

    @Override
    public void desbloqueia(Usuario u, Conteudo p)
    {
    manager.createNativeQuery("delete from item_conteudo_bloqueado where "
            + "cd_post=:post and cd_usuario=:usuario")
            .setParameter("post",p.getCodigo())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void excluiEsteAssuntoDosFavoritos(Usuario u, Assunto t) 
    {
    manager.createNativeQuery("delete from item_usuario_assunto where "
            + "cd_usuario=:usuario and cd_assunto=:assunto")
            .setParameter("usuario",u.getCodigo())
            .setParameter("assunto",t.getCodigo()).executeUpdate();
    }

    @Override
    public void adicionaEsteAssuntoAosFavoritos(Usuario u, Assunto t) 
    {
    manager.createNativeQuery("insert into item_usuario_assunto values"
            + "(:usuario,:assunto)").setParameter("usuario",u.getCodigo())
            .setParameter("assunto",t.getCodigo()).executeUpdate();
    }

    @Override
    public long quantosUsuariosTem()
    {
    return (long)manager.createQuery("SELECT COUNT(u) FROM Usuario u")
            .getSingleResult();
    }
}