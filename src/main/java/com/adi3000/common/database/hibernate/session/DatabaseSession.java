package com.adi3000.common.database.hibernate.session;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.database.hibernate.data.DataObject;


public class DatabaseSession {
	private static final Logger LOGGER = Logger.getLogger(DatabaseSession.class.getName());

	protected Session session;
	private boolean initDbSession;
	private Transaction transaction;
	private boolean willCommit;
	public DatabaseSession(DatabaseSession db){
		setInitDbSession(true);
		this.session = db.getSession();
		this.transaction = db.transaction;
		this.willCommit = db.willCommit;
	}
	
	public DatabaseSession(boolean initDbSession){
		if(initDbSession){
			setInitDbSession(true);
			this.session = this.getSession();
		}else{
			setInitDbSession(false);
		}
		this.willCommit = true;
	}
	public DatabaseSession(){
		this(true);
		this.willCommit = true;
	}
	/**
	 * Return true if a persist is engaged on this session. (It means 
	 * that a transaction has began and has not finished yet)
	 * @return
	 */
	public boolean isSetForCommitting() {
		return transaction != null && transaction.isActive();
	}

	/**
	 * Initialize session for a modification request
	 */
	void initTransaction() {
		openSession();
		if(transaction == null){
			this.transaction = 	this.session.beginTransaction();
		}
	}
	
	/**
	 * Return the Hibernate session. Create it if not opened first
	 * @return
	 */
	public Session getSession(){
		openSession();
		return session;
	}
	

	public void persist(Collection<DataObject> toCommitList){
		initTransaction();
		for(DataObject modelData : toCommitList)
		{
			switch(modelData.getDatabaseOperation())
			{
				case DELETE :
					session.delete(modelData);
					break;
				case INSERT :
					session.save(modelData);
					break;
				case INSERT_OR_UPDATE :
					session.saveOrUpdate(modelData);
					break;
				case UPDATE :
					session.update(modelData);
					break;
				case MERGE :
					session.merge(modelData);
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
				transaction.commit();
			}catch(HibernateException e){
				LOGGER.log(Level.SEVERE,"Can't commit transaction ! ", e);
				rollback();
			}
		}else if (!isWillCommit()){
			LOGGER.fine("Session is not set to commit right now, need to use setWillCommit(true) to commit");
			return true;
		}
		transaction = null;
		return false;		
	}
	
	public boolean rollback(){
		if(isSetForCommitting()){
			transaction.rollback();
			transaction = null;
			return true;
		}
		transaction = null;
		return false;
	}

	public List<AbstractDataObject> getListOfSqlRequest(String sqlRequest)
	{
		openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sqlRequest);
		@SuppressWarnings("unchecked")
		List<AbstractDataObject> list =  sqlQuery.list();
		return list;
	}

	private void openSession()
	{
		if(session == null){
			session = HibernateUtils.getSessionFactory().getCurrentSession();
			initTransaction();
		}
	}

	public boolean closeTransaction(){
		if(transaction != null && isSetForCommitting()){
			if(isWillCommit()){
				transaction.commit();
			}else{
				LOGGER.info("Asked to avoid commit at the end of the transaction");
				transaction.rollback();
			}
			transaction = null;
			return true;
		}
		transaction = null;
		//Nothing to close
		return false;
	}
	public void close(){
		if(closeTransaction()){
			LOGGER.info("You just have close properly a session ");
		}
//		if(session != null){
//			session.close();
//		}
	}

	/**
	 * @return the initDbSession
	 */
	public boolean hasInitDbSession() {
		return initDbSession;
	}

	/**
	 * @param initDbSession the initDbSession to set
	 */
	protected void setInitDbSession(boolean initDbSession) {
		this.initDbSession = initDbSession;
	}

	/**
	 * @return the willCommit
	 */
	public boolean isWillCommit() {
		return willCommit;
	}

	/**
	 * @param willCommit the willCommit to set
	 */
	public void setWillCommit(boolean willCommit) {
		this.willCommit = willCommit;
	}
	
	/**
	 * Create a {@link Query} instance for the given HQL query string.
	 *
	 * @param queryString The HQL query
	 *
	 * @return The query instance for manipulation and execution
	 */
	public Query createQuery(String hqlQuery){
		initTransaction();
		return this.session.createQuery(hqlQuery);
	}

	/**
	 * Create {@link Criteria} instance for the given class (entity or subclasses/implementors).
	 *
	 * @param persistentClass The class, which is an entity, or has entity subclasses/implementors
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(Class<? extends AbstractDataObject> clazz){
		initTransaction();
		return this.session.createCriteria(clazz);
	}
	
	public boolean closeQuery(){
		return closeTransaction();
	}
}
