package br.com.ippie.dao;

import br.com.ippie.negocio.Aprovacao;
import br.com.ippie.negocio.Comentario;
import br.com.ippie.negocio.Concordar;
import br.com.ippie.negocio.Discordar;
import br.com.ippie.negocio.Pesame;
import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeConteudos;
import br.com.ippie.negocio.Reprovacao;
import br.com.ippie.negocio.Usuario;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioConteudo implements RepositorioDeConteudos 
{
@PersistenceContext
private EntityManager manager;
private final AuxiliarJpa jpa;

    public JpaRepositorioConteudo()
    {
    jpa=new AuxiliarJpa();
    }
    
    @Override
    public Collection<Conteudo> rolDeConteudos(Usuario u)
    {
    return preencheAcoesDoConteudo(jpa.conteudos(manager.createNativeQuery("select distinct "
            + "conteudo.cd_conteudo,nm_conteudo,cd_tipo_conteudo,nm_url_link,"
            + "nm_url_video,nm_url_imagem,dt_conteudo,cd_autor,nm_nome,"
            + "nm_sobrenome,nm_foto_perfil,nm_foto_cabecalho,nm_descricao,"
            + "(select count(*) from comentario where "
            + "cd_conteudo=conteudo.cd_conteudo and ic_ativo=true),"
            + "(select group_concat(assunto.cd_assunto,\";\",nm_assunto,\";\","
            + "nm_url_assunto) from assunto inner join item_conteudo_assunto on"
            + " assunto.cd_assunto=item_conteudo_assunto.cd_assunto where \n" 
            + "cd_conteudo=conteudo.cd_conteudo) from conteudo inner join "
            + "item_conteudo_assunto on "
            + "conteudo.cd_conteudo=item_conteudo_assunto.cd_conteudo inner "
            + "join usuario on conteudo.cd_autor=usuario.cd_usuario inner join "
            + "item_usuario_assunto on "
            + "item_conteudo_assunto.cd_assunto=item_usuario_assunto.cd_assunto"
            + " where conteudo.ic_ativo=true and item_conteudo_assunto.cd_conteudo not in (select "
            + "cd_conteudo from item_conteudo_bloqueado where cd_usuario=:cd "
            + "union all select cd_conteudo from conteudo inner join "
            + "item_usuario_bloqueado on "
            + "conteudo.cd_autor=item_usuario_bloqueado.cd_bloqueado where "
            + "cd_bloqueador=:cd union all select cd_conteudo from "
            + "item_conteudo_assunto inner join item_assunto_bloqueado on "
            + "item_conteudo_assunto.cd_assunto=item_assunto_bloqueado.cd_assunto"
            + " where cd_usuario=:cd) and item_usuario_assunto.cd_usuario=:cd order"
            + " by conteudo.cd_conteudo desc limit 500")
            .setParameter("cd",u.getCodigo()).getResultList()));
    }

    @Override
    public Collection<Conteudo> perfil(Usuario u) 
    {
    return preencheAcoesDoConteudo(jpa.perfil(u,manager.createNativeQuery("select "
            + "conteudo.cd_conteudo,nm_conteudo,cd_tipo_conteudo,nm_url_link,"
            + "nm_url_video,nm_url_imagem,(select count(*) from comentario "
            + "where cd_conteudo=conteudo.cd_conteudo and ic_ativo=true),"
            + "(select group_concat(assunto.cd_assunto,\";\",nm_assunto,\";\","
            + "nm_url_assunto) from assunto inner join item_conteudo_assunto on"
            + " assunto.cd_assunto=item_conteudo_assunto.cd_assunto where "
            + "cd_conteudo=conteudo.cd_conteudo) from conteudo where "
            + "cd_autor=:cd and ic_ativo=true order by dt_conteudo desc")
            .setParameter("cd",u.getCodigo()).getResultList()));
    }

    @Override
    public void adiciona(Conteudo p)
    {
    //manager.persist(p);//Adivinha? O JPA não fez o que eu queria. Ele cismou 
    //de, ou deixar o tipo de conteúdo nulo, ou inserir ele novamente no banco de dados. Baita bagulho inútil.
    manager.createNativeQuery("insert into conteudo(nm_conteudo,"
            + "cd_tipo_conteudo,nm_url_link,nm_url_video,nm_url_imagem,"
            + "dt_conteudo,cd_autor) values (:nm,:tipo,:url,:video,:imagem,"
            + "current_timestamp(),:autor)").setParameter("nm",p.getTexto())
            .setParameter("tipo",p.getTipo().getCodigo()).setParameter("url",
                    p.getUrlLink()).setParameter("video",p.getUrlVideo())
            .setParameter("imagem",p.getUrlImagem()).setParameter("autor",
                    p.getAutor().getCodigo()).executeUpdate();
    long cd=((BigInteger)manager.createNativeQuery("select last_insert_id()")
            .getSingleResult()).longValue();
    p.setCodigo(cd);
    p.getAssuntos().stream().forEach((a)->manager.createNativeQuery("insert "
            + "into item_conteudo_assunto values(:conteudo,:assunto)")
            .setParameter("conteudo",p.getCodigo()).setParameter("assunto",a.getCodigo())
            .executeUpdate());
    }

    @Override
    public void edita(Conteudo p) 
    {
    manager.merge(p);
    }

    @Override
    public void exclui(Conteudo p) 
    {
    //manager.remove(p); //Se fizer deste jeito, dá IllegalArgumentException: Removing a detached instance
    manager.createQuery("UPDATE Conteudo p SET p.ativo=false WHERE "
            + "p.codigo=:codigo").setParameter("codigo",p.getCodigo())
            .executeUpdate();
    }

    @Override
    public void aprova(Conteudo p, Aprovacao a) 
    {
    manager.createNativeQuery("insert into aprovacao(cd_conteudo,cd_autor,"
            + "dt_aprovacao) values (:conteudo,:autor,current_timestamp())")
            .setParameter("conteudo",p.getCodigo()).setParameter("autor",a
                    .getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void reprova(Conteudo p, Reprovacao r) 
    {
    manager.createNativeQuery("insert into reprovacao(cd_conteudo,cd_autor,"
            + "dt_reprovacao) values (:conteudo,:autor,current_timestamp())")
            .setParameter("conteudo",p.getCodigo()).setParameter("autor",r
                    .getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void comenta(Conteudo p, Comentario c) 
    {//O problema está que o comentário não sabe a qual conteúdo ele pertence, 
    //logo o manager não vai saber o valor do cd_conteudo, que não pod ser nulo.
    manager.createNativeQuery("insert into comentario(nm_comentario,"
            + "nm_url_imagem,dt_comentario,cd_conteudo,cd_autor) values "
            + "(:texto,:url,current_timestamp(),:conteudo,:autor)")
            .setParameter("texto",c.getTexto())
            .setParameter("url",c.getUrlImagem())
            .setParameter("conteudo",p.getCodigo())
            .setParameter("autor",c.getAutor().getCodigo())
            .executeUpdate();
    }

    @Override
    public Collection<Comentario> comentarios(Conteudo p) 
    {
    return jpa.comentarios(manager.createNativeQuery("select cd_comentario,"
            + "nm_comentario,nm_url_imagem,dt_comentario,cd_autor,nm_nome,"
            + "nm_sobrenome,nm_foto_perfil,nm_foto_cabecalho,nm_descricao from "
            + "comentario inner join usuario on "
            + "comentario.cd_autor=usuario.cd_usuario where cd_conteudo=:conteudo"
            + " and comentario.ic_ativo=true").setParameter("conteudo",p.getCodigo())
            .getResultList());
    }

    @Override
    public void exclui(Conteudo p, Aprovacao a) 
    {//O problema é que a aprovação não vai ter chave primária definida aqui.
    manager.createNativeQuery("delete from aprovacao where cd_conteudo=:conteudo and "
            + "cd_autor=:autor").setParameter("conteudo",p.getCodigo())
            .setParameter("autor",a.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void exclui(Conteudo p, Reprovacao r) 
    {
    manager.createNativeQuery("delete from reprovacao where cd_conteudo=:conteudo and "
            + "cd_autor=:autor").setParameter("conteudo",p.getCodigo())
            .setParameter("autor",r.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public Collection<Conteudo> apenasConteudosComFotos(Usuario u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exclui(Comentario c, Aprovacao a) 
    {
    manager.createNativeQuery("delete from aprovacao where cd_comentario=:id "
            + "and cd_autor=:autor").setParameter("id",c.getCodigo())
            .setParameter("autor",a.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void aprova(Comentario c, Aprovacao a) 
    {
    manager.createNativeQuery("insert into aprovacao(cd_comentario,cd_autor,"
            + "dt_aprovacao) values (:id,:autor,current_timestamp())")
            .setParameter("id",c.getCodigo()).setParameter("autor",a.getAutor()
                    .getCodigo()).executeUpdate();
    }

    @Override
    public void reprova(Comentario c, Reprovacao r) 
    {
    manager.createNativeQuery("insert into reprovacao(cd_comentario,cd_autor,"
            + "dt_reprovacao) values (:id,:autor,current_timestamp())")
            .setParameter("id",c.getCodigo()).setParameter("autor",r.getAutor()
                    .getCodigo()).executeUpdate();
    }

    @Override
    public void exclui(Comentario c, Reprovacao r) 
    {
    manager.createNativeQuery("delete from reprovacao where cd_comentario=:id "
            + "and cd_autor=:autor").setParameter("id",c.getCodigo())
            .setParameter("autor",r.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void edita(Comentario c) 
    {
    manager.merge(c);
    }

    @Override
    public void exclui(Comentario c) 
    {
    //manager.remove(c); Removing a detached instance
    manager.createQuery("UPDATE Comentario c SET c.ativo=false WHERE "
            + "c.codigo=:codigo").setParameter("codigo",c.getCodigo())
            .executeUpdate();
    }

    @Override
    public void pesames(Conteudo p, Pesame pe) 
    {
    manager.createNativeQuery("insert into pesame(cd_conteudo,cd_autor,dt_pesame) "
            + "values (:conteudo,:autor,current_timestamp())").setParameter("conteudo",
                    p.getCodigo()).setParameter("autor",pe.getAutor()
                            .getCodigo()).executeUpdate();
    }

    @Override
    public void exclui(Conteudo p, Pesame pe) 
    {
    manager.createNativeQuery("delete from pesame where cd_conteudo=:conteudo and "
            + "cd_autor=:autor").setParameter("conteudo",p.getCodigo())
            .setParameter("autor",pe.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void concorda(Conteudo p, Concordar c) 
    {
    manager.createNativeQuery("insert into concordar(cd_conteudo,cd_autor,"
            + "dt_concordar) values (:conteudo,:autor,current_timestamp())")
            .setParameter("conteudo",p.getCodigo()).setParameter("autor",c
                    .getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void exclui(Conteudo p, Concordar c) 
    {
    manager.createNativeQuery("delete from concordar where cd_conteudo=:conteudo and "
            + "cd_autor=:autor").setParameter("conteudo",p.getCodigo())
            .setParameter("autor",c.getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void discorda(Conteudo p, Discordar d) 
    {
    manager.createNativeQuery("insert into discordar(cd_conteudo,cd_autor,"
            + "dt_discordar) values (:conteudo,:autor,current_timestamp())")
            .setParameter("conteudo",p.getCodigo()).setParameter("autor",d
                    .getAutor().getCodigo()).executeUpdate();
    }

    @Override
    public void exclui(Conteudo p, Discordar d) 
    {
    manager.createNativeQuery("delete from discordar where cd_conteudo=:conteudo and "
            + "cd_autor=:autor").setParameter("conteudo",p.getCodigo())
            .setParameter("autor",d.getAutor().getCodigo()).executeUpdate();
    }
    
    @Override
    public boolean estaAprovado(Conteudo p, Aprovacao a) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from aprovacao where"
            + " cd_conteudo=:conteudo and cd_autor=:usuario")
            .setParameter("conteudo",p.getCodigo()).setParameter("usuario",a
                    .getAutor().getCodigo()).getSingleResult()).intValue()==1;
    }

    @Override
    public boolean estaReprovado(Conteudo p, Reprovacao r) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from reprovacao where"
            + " cd_conteudo=:conteudo and cd_autor=:usuario")
            .setParameter("conteudo",p.getCodigo()).setParameter("usuario",r
                    .getAutor().getCodigo()).getSingleResult()).intValue()==1;
    }

    @Override
    public boolean estaConcordado(Conteudo p, Concordar c) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from concordar where"
            + " cd_conteudo=:conteudo and cd_autor=:usuario").setParameter("conteudo",p.getCodigo())
            .setParameter("usuario",c.getAutor().getCodigo()).getSingleResult())
            .intValue()==1;
    }

    @Override
    public boolean estaDiscordado(Conteudo p, Discordar d) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from discordar where"
            + " cd_conteudo=:conteudo and cd_autor=:usuario")
            .setParameter("conteudo",p.getCodigo()).setParameter("usuario",d
                    .getAutor().getCodigo()).getSingleResult()).intValue()==1;
    }

    @Override
    public boolean estaComPesame(Conteudo p, Pesame pe) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from pesame where"
            + " cd_conteudo=:conteudo and cd_autor=:usuario").setParameter("conteudo",p.getCodigo())
            .setParameter("usuario",pe.getAutor().getCodigo())
            .getSingleResult()).intValue()==1;
    }

    @Override
    public int quantasAprovacoes(Conteudo p) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "aprovacao where cd_conteudo=:conteudo")
            .setParameter("conteudo",p.getCodigo()).getSingleResult()).intValue();
    }

    @Override
    public int quantasReprovacoes(Conteudo p) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "reprovacao where cd_conteudo=:conteudo").setParameter("conteudo",p
                    .getCodigo()).getSingleResult()).intValue();
    }

    @Override
    public int quantosPesames(Conteudo p) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "pesame where cd_conteudo=:conteudo").setParameter("conteudo",p.getCodigo())
            .getSingleResult()).intValue();
    }

    @Override
    public int quantosConcordar(Conteudo p)
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "concordar where cd_conteudo=:conteudo").setParameter("conteudo",p.getCodigo())
            .getSingleResult()).intValue();
    }

    @Override
    public int quantosDiscordar(Conteudo p) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "discordar where cd_conteudo=:conteudo").setParameter("conteudo",p.getCodigo())
            .getSingleResult()).intValue();
    }

    @Override
    public int quantosComentarios(Conteudo p) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "comentario where cd_conteudo=:conteudo").setParameter("conteudo",p.getCodigo())
            .getSingleResult()).intValue();
    }
    
    @Override
    public Collection<Conteudo> notificacoes(Usuario u) 
    {
    return preencheAcoesDoConteudo(jpa.notificacoes(manager
            .createNativeQuery("select conteudo.cd_conteudo,nm_conteudo,"
                    + "cd_tipo_conteudo,nm_url_link,nm_url_video,nm_url_imagem,"
                    + "cd_autor,nm_nome,nm_sobrenome,nm_foto_perfil,"
                    + "nm_foto_cabecalho,nm_descricao,(select count(*) from "
                    + "comentario where cd_conteudo=conteudo.cd_conteudo and "
                    + "ic_ativo=true) from conteudo inner join usuario on "
                    + "conteudo.cd_autor=usuario.cd_usuario inner join "
                    + "item_usuario_notificacao on conteudo.cd_conteudo=item_usuario_notificacao.cd_conteudo"
                    + " where item_usuario_notificacao.cd_usuario=:cd and "
                    + "conteudo.ic_ativo=true order by dt_notificacao desc")
            .setParameter("cd",u.getCodigo()).getResultList()));
    }

    @Override
    public void notifica(Usuario u, Conteudo c)
    {
      if(!esteConteudoEhNotificacao(c,u))
      {
      manager.createNativeQuery("insert into item_usuario_notificacao"
              + "(cd_usuario,cd_conteudo,dt_notificacao) values"
              + "(:usuario,:conteudo,current_timestamp())")
              .setParameter("usuario",u.getCodigo())
              .setParameter("conteudo",c.getCodigo()).executeUpdate();
      }
      else
      {
      manager.createNativeQuery("update item_usuario_notificacao set "
              + "dt_notificacao=current_timestamp() where cd_usuario=:usuario "
              + "and cd_conteudo=:conteudo")
              .setParameter("usuario",u.getCodigo())
              .setParameter("conteudo",c.getCodigo()).executeUpdate();
      }
    }

    @Override
    public void notificacoesVista(Usuario u) 
    {
    manager.createNativeQuery("update item_usuario_notificacao set "
            + "ic_visto=true where cd_usuario=:usuario")
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public boolean haNotificacoesNaoVistas(Usuario u) 
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "item_usuario_notificacao where cd_usuario=:usuario and "
            + "ic_visto=false").setParameter("usuario",u.getCodigo())
            .getSingleResult()).intValue()!=0;
    }

    @Override
    public boolean esteConteudoEhNotificacao(Conteudo c, Usuario u)
    {
    return ((BigInteger)manager.createNativeQuery("select count(*) from "
            + "item_usuario_notificacao where cd_usuario=:usuario and "
            + "cd_conteudo=:conteudo").setParameter("usuario",u.getCodigo())
            .setParameter("conteudo",c.getCodigo()).getSingleResult())
            .intValue()!=0;    
    }
    
    private Collection<Conteudo> preencheAcoesDoConteudo(Collection<Conteudo> n)
    {
    n.stream().forEach((c)->
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
    return n;
    }
}