package com.assure.dao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    private EntityManager em;

    public <T> T insert(T b) {
        em.persist(b);
        em.flush();
        return b;
    }

    public <T> void delete(Class<T> c, Long id) {
        T p = em.find(c, id);
        em.remove(p);
    }

    public <T> T select(Class<T> c, Long id) {
        return em.find(c, id);
    }

    public <T> T update(T b) {
        em.merge(b);
        em.flush();
        return b;
    }

    // Gives single result from database if exists or null
    protected <T> T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    // Creates query
    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> c) {
        return em.createQuery(jpql, c);
    }

    // Returns entity manager instance
    protected EntityManager em() {
        return em;
    }

}