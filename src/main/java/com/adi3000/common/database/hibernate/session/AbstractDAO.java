package com.adi3000.common.database.hibernate.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.database.hibernate.data.DataObject;

public abstract class AbstractDAO<T extends DataObject> implements DAO<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8495076397653964860L;
	/**
	 * 
	 */
	@Inject
	private transient SessionFactory sessionFactory;
	
	private final Class<T> clazz;
	
	public AbstractDAO(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public T get(Serializable id){
		@SuppressWarnings("unchecked")
		T data = (T)getSession().get(clazz, id);
		return data ;
	}
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession(){
		return  sessionFactory.getCurrentSession();
	}
	
	public void persist(Collection<DataObject> toCommitList){
		for(DataObject modelData : toCommitList)
		{
			switch(modelData.getDatabaseOperation())
			{
				case DELETE :
					getSession().delete(modelData);
					break;
				case INSERT :
					getSession().save(modelData);
					break;
				case INSERT_OR_UPDATE :
					getSession().saveOrUpdate(modelData);
					break;
				case UPDATE :
					getSession().update(modelData);
					break;
				case MERGE :
					getSession().merge(modelData);
					break;
				case NO_ACTION:
				default :
					break;
			}
		}
	}

	public void persist(DataObject modelData){
		persist(Collections.singleton(modelData));
	}
	
	public void modify(T data){
		if(data.getDatabaseOperation() == DatabaseOperation.DEFAULT){
			if(data.getId() == null){
				data.setDatabaseOperation(DatabaseOperation.INSERT);
			}else{
				data.setDatabaseOperation(DatabaseOperation.UPDATE);
			}
		}
		persist(data);
	}
	
	public Collection<T>  modifyDataObject(Collection<T> data){
		Set<T> dataSet = null;
		if(data instanceof Set){
			dataSet = (Set<T>) data;
		}else{
			dataSet  = new HashSet<T>(data);
		}
		for(T datum : dataSet){
			if(datum != null){
				modify(datum);
			}
		}
		return data;
	}
	public T  modifyDataObject(T data){
		modify(data);
		return data;
	}
	
	public void deleteDataObject(T data){
		data.setDatabaseOperation(DatabaseOperation.DELETE);
		persist(data);
	}
	
	public T getDataObject(AbstractDataObject model)
	{
		@SuppressWarnings("unchecked")
		T toReturn = (T)getSession().get(model.getClass(), model.getId());

		return toReturn;
	}
	
	/**
	 * Create a {@link Query} instance for the given HQL query string.
	 *
	 * @param queryString The HQL query
	 *
	 * @return The query instance for manipulation and execution
	 */
	public Query createQuery(String hqlQuery){
		return getSession().createQuery(hqlQuery);
	}

	/**
	 * Create {@link Criteria} instance for the given class (entity or subclasses/implementors).
	 *
	 * @param persistentClass The class, which is an entity, or has entity subclasses/implementors
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(Class<? extends AbstractDataObject> clazz){
		return getSession().createCriteria(clazz);
	}
	
	/**
	 * Create a {@link SQLQuery} instance for the given SQL query string.
	 *
	 * @param queryString The SQL query
	 * 
	 * @return The query instance for manipulation and execution
	 */
	public SQLQuery createSQLQuery(String sql){
		return getSession().createSQLQuery(sql);
	}
}
