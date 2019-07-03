package br.com.ippie.dao;

import br.com.ippie.negocio.Candidatura;
import br.com.ippie.negocio.RepositorioDeCoisasAvulsas;
import br.com.ippie.negocio.Sugestao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioCoisasAvulsas implements RepositorioDeCoisasAvulsas 
{
@PersistenceContext
private EntityManager manager;

    @Override
    public void adicionaSugestao(Sugestao s) 
    {
    manager.persist(s);
    }

    @Override
    public void adicionaCandidatura(Candidatura c) 
    {
    manager.persist(c);
    }
}