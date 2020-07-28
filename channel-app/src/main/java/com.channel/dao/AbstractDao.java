package com.channel.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public <T> T insert(T b) {
        em.persist(b);
        em.flush();
        return b;
    }

    @Transactional
    public <T> void delete(Class<T> c, Long id) {
        T p = em.find(c, id);
        em.remove(p);
    }

    @Transactional(readOnly = true)
    public <T> T select(Class<T> c, Long id) {
        return em.find(c, id);
    }

    @Transactional
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