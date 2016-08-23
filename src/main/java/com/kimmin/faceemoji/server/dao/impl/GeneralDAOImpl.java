package com.kimmin.faceemoji.server.dao.impl;

import com.kimmin.faceemoji.server.dao.GeneralDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by fowafolo on 15/5/18.
 */
public abstract class GeneralDAOImpl<T> implements GeneralDAO<T> {
    private Class<T> entityClass;

    public GeneralDAOImpl(Class<T> classes){
        this.entityClass = classes;
//        Configuration cfg = new Configuration();
//        sessionFactory = cfg.buildSessionFactory();
    }

    @Autowired
    protected SessionFactory sessionFactory;

    public void insert(T t){
        sessionFactory.getCurrentSession().save(t);
    }

    public void delete(T t){
        sessionFactory.getCurrentSession().delete(t);
    }
    public void update(T t){
        sessionFactory.getCurrentSession().update(t);
    }

    @SuppressWarnings("unchecked")
    public T queryById(String id){
        return (T) sessionFactory.getCurrentSession().get(entityClass,id);
    }
    public List<T> queryAll(){
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(entityClass);
        return criteria.list();
    }
    public void deleteById(String id){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        T t = (T) session.get(entityClass, id);
        session.delete(t);
        tx.commit();
    }

}
