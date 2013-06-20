package com.adi3000.common.database.hibernate.session;

import org.hibernate.Session;
import org.hibernate.Transaction;

class HibernateSession {
	private Session session;
	private boolean initDbSession;
	private Transaction transaction;
	private boolean commitOnClose;
	private boolean willCommit;
	
	HibernateSession(boolean commitOnClose, boolean initDbSession) {
		this.initDbSession = initDbSession;
		this.commitOnClose = commitOnClose;
		this.willCommit = !commitOnClose;
	}
	/**
	 * @return the initDbSession
	 */
	boolean isInitDbSession() {
		return initDbSession;
	}
	/**
	 * @param initDbSession the initDbSession to set
	 */
	void setInitDbSession(boolean initDbSession) {
		this.initDbSession = initDbSession;
	}
	/**
	 * @return the transaction
	 */
	Transaction getTransaction() {
		return transaction;
	}
	/**
	 * @param transaction the transaction to set
	 */
	void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	/**
	 * @return the willCommit
	 */
	boolean isWillCommit() {
		return willCommit;
	}
	/**
	 * @param willCommit the willCommit to set
	 */
	void setWillCommit(boolean willCommit) {
		this.willCommit = willCommit;
	}
	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	/**
	 * @return the commitOnClose
	 */
	public boolean isCommitOnClose() {
		return commitOnClose;
	}
	/**
	 * @param commitOnClose the commitOnClose to set
	 */
	public void setCommitOnClose(boolean commitOnClose) {
		this.commitOnClose = commitOnClose;
	}
}
