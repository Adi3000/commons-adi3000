package com.adi3000.common.database.hibernate.session;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.database.hibernate.data.DataObject;


public class DatabaseSession {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSession.class);

	private HibernateSession hibernateSession;
	public DatabaseSession(DatabaseSession db){
		this.hibernateSession = db.hibernateSession;
	}
	
	/**
	 * initDbSession has no effect as it not needed for now use DatabaseSession(commitOnClose)
	 * @param commitOnClose
	 * @param initDbSession
	 */
	private DatabaseSession(boolean commitOnClose, boolean initDbSession){
		this.hibernateSession = new HibernateSession(commitOnClose, initDbSession);
		openSession();
	}
	/**
	 * Commit transaction at the end of the treatment (when you close it)
	 * @param commitOnClose
	 */
	public DatabaseSession(boolean commitOnClose){
		this(commitOnClose, true);
	}
	public DatabaseSession(){
		this(true);
	}
	/**
	 * Return true if a persist is engaged on this session. (It means 
	 * that a transaction has began and has not finished yet)
	 * @return
	 */
	public boolean isSetForCommitting() {
		return hibernateSession.getTransaction() != null && hibernateSession.getTransaction().isActive();
	}

	private void initTransaction() {
		if(hibernateSession.getTransaction() == null){
			hibernateSession.setTransaction(getSession().beginTransaction());
		}
	}
	
	/**
	 * Return the Hibernate session.
	 * @return
	 */
	public Session getSession(){
		return hibernateSession.getSession();
	}
	
	public void persist(Collection<DataObject> toCommitList){
		initTransaction();
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
	
	/**
	 * Commit if willCommit is set to true (default value) and a session is prepared for commit (isSetForCommitting())
	 * @return true if willCommit is set to false (nothing bad happen, but will be committed next time), true if commit is done, otherwise false
	 *  
	 */
	public boolean sendForCommit(){
		if(isWillCommit() && isSetForCommitting()){
			try{
				hibernateSession.getTransaction().commit();
			}catch(HibernateException e){
				LOGGER.error("Can't commit transaction ! ", e);
				rollback();
			}
		}else if (!isWillCommit()){
			LOGGER.trace("Session is not set to commit right now, need to use setWillCommit(true) to commit");
			return true;
		}
		hibernateSession.setTransaction(null);
		return false;		
	}
	
	public boolean rollback(){
		if(isSetForCommitting()){
			hibernateSession.getTransaction().rollback();
			hibernateSession.setTransaction(null);
			return true;
		}
		hibernateSession.setTransaction(null);
		return false;
	}

	private void openSession()
	{
		if(hibernateSession.getSession() == null){
			hibernateSession.setSession(HibernateUtils.getSessionFactory().getCurrentSession());
			initTransaction();
		}
	}

	public boolean closeTransaction(){
		if(hibernateSession.getTransaction() != null && isSetForCommitting()){
			if(isWillCommit()){
				hibernateSession.getTransaction().commit();
			}else{
				LOGGER.info("Asked to avoid commit at the end of the transaction");
				hibernateSession.getTransaction().rollback();
			}
			hibernateSession.setTransaction(null);
			return true;
		}
		hibernateSession.setTransaction(null);
		//Nothing to close
		return false;
	}
	/**
	 * Close transaction with a commit if willSetCommit is set to true or rollback otherwise
	 */
	public void close(){
		//If asked to commit on close set the next try to commit to true
		hibernateSession.setWillCommit(hibernateSession.isCommitOnClose());
		if(closeTransaction()){
			LOGGER.trace("You just have close properly a session ");
		}
//		if(session != null){
//			session.close();
//		}
	}

	/**
	 * @return the initDbSession
	 */
	public boolean hasInitDbSession() {
		return hibernateSession.isInitDbSession();
	}

	/**
	 * @param initDbSession the initDbSession to set
	 */
	protected void setInitDbSession(boolean initDbSession) {
		hibernateSession.setInitDbSession(initDbSession);
	}

	/**
	 * @return the willCommit
	 */
	public boolean isWillCommit() {
		return hibernateSession.isWillCommit();
	}

	/**
	 * @param willCommit the willCommit to set
	 */
	public void setWillCommit(boolean willCommit) {
		hibernateSession.setWillCommit(willCommit);
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
