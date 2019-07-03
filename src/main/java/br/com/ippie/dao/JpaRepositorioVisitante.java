package br.com.ippie.dao;

import br.com.ippie.negocio.RepositorioDeVisitante;
import br.com.ippie.negocio.Visitante;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRepositorioVisitante implements RepositorioDeVisitante {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void adiciona(Visitante v) 
    {
    em.persist(v);
    }
}