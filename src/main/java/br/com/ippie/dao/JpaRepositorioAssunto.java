package br.com.ippie.dao;

import br.com.ippie.negocio.RepositorioDeAssuntos;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.Usuario;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioAssunto implements RepositorioDeAssuntos 
{
@PersistenceContext
private EntityManager manager;
private final AuxiliarJpa jpa;

    public JpaRepositorioAssunto() 
    {
    jpa=new AuxiliarJpa();
    }
    
    @Override
    public void adiciona(Assunto t)
    {
    manager.persist(t);
    }

    @Override
    public void edita(Assunto t) 
    {
    manager.merge(t);
    }

    @Override
    public Collection<Assunto> listaAssuntosAleatoriamente(int max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Assunto> listaAssuntos(Usuario u) 
    {
    return jpa.assuntos(manager.createNativeQuery("select assunto.cd_assunto,"
            + "nm_assunto,nm_url_assunto,dt_assunto from assunto inner join "
            + "item_usuario_assunto on assunto.cd_assunto=item_usuario_assunto."
            + "cd_assunto where cd_usuario=:cd").setParameter("cd",u.getCodigo())
            .getResultList());
    }

    @Override
    public void exclui(Assunto t) 
    {
    //manager.remove(t);/Removing a detached instance br.com.ippie.negocio.Assunto
    manager.createNativeQuery("delete from item_conteudo_assunto where cd_assunto=:cd")
            .setParameter("cd",t.getCodigo()).executeUpdate();
    manager.createNativeQuery("delete from item_usuario_assunto where cd_assunto=:cd")
            .setParameter("cd",t.getCodigo()).executeUpdate();
    manager.createNativeQuery("delete from assunto where cd_assunto=:cd")
            .setParameter("cd",t.getCodigo()).executeUpdate();
    }

    @Override
    public Collection<Assunto> listaAssuntos(String nome)
    {
    return jpa.assuntos(manager.createNativeQuery("select assunto.cd_assunto,"
            + "nm_assunto,nm_url_assunto,dt_assunto from assunto where "
            + "nm_assunto like :nome").setParameter("nome",nome+"%")
            .getResultList());
    }

    @Override
    public Collection<Assunto> listaAssuntosMaisUsadosPorUsuarios(int max) 
    {
    return jpa.assuntos(manager.createNativeQuery("select assunto.cd_assunto,"
            + "nm_assunto,nm_url_assunto,dt_assunto from assunto inner join "
            + "item_usuario_assunto on assunto.cd_assunto=item_usuario_assunto.cd_assunto "
            + "group by item_usuario_assunto.cd_assunto order by count(*) desc")
            .setMaxResults(max).getResultList());
    }

    @Override
    public Collection<Assunto> listaAssuntosMaisUsadosPorConteudos(int max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Conteudo> conteudosComEsteAssunto(Assunto a) 
    {
    Collection<Conteudo> conteudos=jpa.conteudos(manager.createNativeQuery("select "
            + "conteudo.cd_conteudo,"
            + "nm_conteudo,cd_tipo_conteudo,nm_url_link,nm_url_video,"
            + "nm_url_imagem,dt_conteudo,cd_autor,nm_nome,nm_sobrenome,"
            + "nm_foto_perfil,nm_foto_cabecalho,nm_descricao,(select count(*) "
            + "from comentario where cd_conteudo=conteudo.cd_conteudo and "
            + "ic_ativo=true),(select group_concat(assunto.cd_assunto,\";\","
            + "nm_assunto,\";\",nm_url_assunto) from assunto inner join "
            + "item_conteudo_assunto on "
            + "assunto.cd_assunto=item_conteudo_assunto.cd_assunto where "
            + "cd_conteudo=conteudo.cd_conteudo) from conteudo inner join "
            + "usuario on conteudo.cd_autor=usuario.cd_usuario inner join "
            + "item_conteudo_assunto on conteudo.cd_conteudo=item_conteudo_assunto.cd_conteudo "
            + "where cd_assunto=:assunto").setParameter("assunto",a.getCodigo())
            .setMaxResults(10).getResultList());
      conteudos.stream().forEach((c)->
      {
        switch (c.getTipo().getNome()) 
        {
          case "Normal":
          c.setAprovacoes(jpa.aprovacoes(manager.createNativeQuery("select cd_autor"
                  + " from aprovacao where cd_conteudo=:conteudo")
                  .setParameter("conteudo",c.getCodigo()).getResultList()));
          c.setReprovacoes(jpa.reprovacoes(manager.createNativeQuery("select "
                  + "cd_autor from reprovacao where cd_conteudo=:conteudo")
                  .setParameter("conteudo",c.getCodigo()).getResultList()));
          break;
          case "Morte":
          c.setPesames(jpa.pesames(manager.createNativeQuery("select cd_autor from "
                  + "pesame where cd_conteudo=:conteudo")
                  .setParameter("conteudo",c.getCodigo()).getResultList()));
          break;
          case "Opiniao":
          c.setConcordar(jpa.concordar(manager.createNativeQuery("select cd_autor "
                  + "from concordar where cd_conteudo=:conteudo")
                  .setParameter("conteudo",c.getCodigo()).getResultList()));
          c.setDiscordar(jpa.discordar(manager.createNativeQuery("select cd_autor "
                  + "from discordar where cd_conteudo=:conteudo")
                  .setParameter("conteudo",c.getCodigo()).getResultList()));
          break;
        }
      });
    return conteudos;
    }

    @Override
    public Collection<Assunto> listaAssuntosCriadosPor(Usuario u) 
    {
    Collection<Assunto> assuntos=jpa.assuntos(manager.createNativeQuery("select cd_assunto,nm_assunto,"
            + "nm_url_assunto,dt_assunto from assunto where cd_autor=:cd "
            + "order by dt_assunto desc")
            .setParameter("cd",u.getCodigo()).getResultList());
      assuntos.stream().forEach((a)->
      {
      a.setAutor(u);
      });
    return assuntos;
    }

    @Override
    public boolean existeEsteAssunto(String nome) 
    {
    return ((BigInteger)manager
            .createNativeQuery("select count(*) from assunto where nm_assunto=:nome")
            .setParameter("nome",nome).getSingleResult()).longValue()!=0;
    }
}