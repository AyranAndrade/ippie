package br.com.ippie.dao;

import br.com.ippie.negocio.Acesso;
import br.com.ippie.negocio.RepositorioDeAcessos;
import java.math.BigInteger;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioAcesso implements RepositorioDeAcessos 
{
@PersistenceContext
private EntityManager manager;
    
    @Override
    public void adiciona(Acesso a) 
    {
    Date entrada=Date.from(a.getDataEntrada().atZone(ZoneId.systemDefault())
            .toInstant());
    manager.createNativeQuery("insert into acesso(dt_entrada,cd_usuario) "
            + "values(:entrada,:usuario)").setParameter("entrada",entrada)
            .setParameter("usuario",a.getUsuario().getCodigo()).executeUpdate();
    a.setCodigo(((BigInteger)(manager.createNativeQuery("select last_insert_id()")
            .getSingleResult())).longValue());
    }

    @Override
    public void finaliza(Acesso a) 
    {
    Date saida=Date.from(a.getDataSaida().atZone(ZoneId.systemDefault()).toInstant());
    manager.createNativeQuery("update acesso set dt_saida=:saida where "
            + "cd_acesso=:acesso").setParameter("saida",saida)
            .setParameter("acesso",a.getCodigo()).executeUpdate();
    }
}