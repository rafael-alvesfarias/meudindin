package br.com.mdd.persistence.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.mdd.persistence.dao.GenericDAO;

@Repository(value = "genericDAO")
public class GenericHibernateDAO<T> implements GenericDAO<T> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(T t) {
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}

	@Override
	@Transactional
	public void remove(T t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public T find(Serializable id, Class<T> clazz) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<T> findAll(Class<T> clazz) {
		List<T> list = sessionFactory.getCurrentSession().
			createCriteria(clazz)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		return list;
	}
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
