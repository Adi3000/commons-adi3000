package com.adi3000.common.database.hibernate.session;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.database.hibernate.data.DataObject;

public interface DAO<T extends DataObject> extends Serializable{
	
	public Session getSession();
	
	public void persist(Collection<DataObject> toCommitList);

	public void persist(DataObject modelData);
	
	/**
	 * Update, delete or insert data according to the {@link DataObject}.dataOperation attribute
	 * @param data
	 */
	public void modify(T data);

	/**
	 * Update, delete or insert a {@link Collection} of {@link DataObject} according to the {@link DataObject}.dataOperation attribute
	 * @param data
	 * @return
	 */
	public Collection<T>  modifyDataObject(Collection<T> data);
	/**
	 * Update, delete or insert data according to the {@link DataObject}.dataOperation attribute
	 * returning the updated data
	 * @param data
	 * @return
	 */
	public T  modifyDataObject(T data);

	/**
	 * Delet a {@link DataObject}
	 * @param data
	 */
	public void deleteDataObject(T data);
	
	/**
	 * Retrive {@link DataObject} by its {@code id}
	 * @param model
	 * @return
	 */
	public T get(Serializable id);
	
	public T getDataObject(AbstractDataObject model);
	
	/**
	 * Create a {@link Query} instance for the given HQL query string.
	 *
	 * @param queryString The HQL query
	 *
	 * @return The query instance for manipulation and execution
	 */
	public Query createQuery(String hqlQuery);

	/**
	 * Create {@link Criteria} instance for the given class (entity or subclasses/implementors).
	 *
	 * @param persistentClass The class, which is an entity, or has entity subclasses/implementors
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(Class<? extends AbstractDataObject> clazz);
	
	/**
	 * Create a {@link SQLQuery} instance for the given SQL query string.
	 *
	 * @param queryString The SQL query
	 * 
	 * @return The query instance for manipulation and execution
	 */
	public SQLQuery createSQLQuery(String sql);
}
