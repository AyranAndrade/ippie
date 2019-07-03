package br.com.ippie.dao;

import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeDenuncias;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Usuario;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioDenuncia implements RepositorioDeDenuncias 
{
@PersistenceContext
private EntityManager manager;
private final AuxiliarJpa jpa;

    public JpaRepositorioDenuncia()
    {
    jpa=new AuxiliarJpa();
    }

    @Override
    public void denuncia(Conteudo p, Usuario u) 
    {
    manager.createNativeQuery("insert into item_conteudo_denunciado values(:conteudo,"
            + ":usuario,current_timestamp())").setParameter("conteudo",p.getCodigo())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public void denuncia(Assunto t, Usuario u)
    {
    manager.createNativeQuery("insert into item_assunto_denunciado values(:assunto,"
            + ":usuario,current_timestamp())").setParameter("assunto",t.getCodigo())
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }

    @Override
    public Collection<Conteudo> conteudosDenunciados()
    {//Tá liberado usar o HashSet, pois ele vai comprar primeiro a chave primária
    return jpa.conteudosParaDenuncia(manager.createNativeQuery("select distinct conteudo.cd_conteudo,nm_conteudo,"
            + "nm_url_link,nm_url_video,nm_url_imagem from conteudo inner join "
            + "item_conteudo_denunciado on "
            + "conteudo.cd_conteudo=item_conteudo_denunciado.cd_conteudo").getResultList());
    }

    @Override
    public Collection<Assunto> assuntosDenunciados()
    {
    return jpa.assuntos(manager.createNativeQuery("select distinct assunto.cd_assunto,nm_assunto,"
            + "nm_url_assunto,dt_assunto from assunto inner join "
            + "item_assunto_denunciado on assunto.cd_assunto=item_assunto_denunciado.cd_assunto")
            .getResultList());
    }

    @Override
    public void denunciaVerificada(Conteudo p)
    {
    manager.createNativeQuery("delete from item_conteudo_denunciado where "
            + "cd_conteudo=:conteudo").setParameter("conteudo",p.getCodigo()).executeUpdate();
    }

    @Override
    public void denunciaVerificada(Assunto t)
    {
    manager.createNativeQuery("delete from item_assunto_denunciado where "
            + "cd_assunto=:assunto").setParameter("assunto",t.getCodigo()).executeUpdate();
    }

    @Override
    public void denuncia(String nomeAssunto, Usuario u) 
    {
    long cd=((BigInteger)manager.createNativeQuery("select cd_assunto from "
            + "assunto where nm_assunto=:nome").setParameter("nome",nomeAssunto)
            .getSingleResult()).longValue();
    manager.createNativeQuery("insert into item_assunto_denunciado values(:assunto,"
            + ":usuario,current_timestamp())").setParameter("assunto",cd)
            .setParameter("usuario",u.getCodigo()).executeUpdate();
    }
}