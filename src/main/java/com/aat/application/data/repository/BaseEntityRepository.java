package com.aat.application.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Repository
public class BaseEntityRepository<T> {
    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entityClass;

    public BaseEntityRepository() {
        this.entityClass = null;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<T> getEntityClass() {
        if (entityClass == null) {
            Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                entityClass = (Class<T>) actualTypeArguments[0];
            } else {
                throw new IllegalArgumentException("Unable to determine entity class type.");
            }
        }
        return entityClass;
    }

    public List<T> findAll(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e", entityClass);
            return query.getResultList();
        } else {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e WHERE lower(e.name) LIKE lower(:filter)", entityClass);
            query.setParameter("filter", "%" + stringFilter + "%");
            return query.getResultList();
        }
    }

    private String getEntityClassName() {
        return getEntityClass().getSimpleName();
    }

    public T saveEntity(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public void deleteEntity(T entity) {
        entityManager.remove(entity);
    }
}