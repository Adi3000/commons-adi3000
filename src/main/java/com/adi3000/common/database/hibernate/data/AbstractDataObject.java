package com.adi3000.common.database.hibernate.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.adi3000.common.database.hibernate.DatabaseOperation;

public abstract class AbstractDataObject implements DataObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseOperation databaseOperation;
	public AbstractDataObject(){
		this.databaseOperation = DatabaseOperation.DEFAULT; 
	}

	/**
	 * @param databaseOperation the databaseOperation to set
	 */
	public void setDatabaseOperation(DatabaseOperation databaseOperation) {
		this.databaseOperation = databaseOperation;
	}

	/**
	 * @return the databaseOperation
	 */
	@XmlTransient
	public DatabaseOperation getDatabaseOperation() {
		return databaseOperation;
	}
	
	public abstract Serializable getId();

	public String toString(){
		return this.getId().toString();
	}
	
	@Override
	public int hashCode(){
		if(getId() != null){
			return getId().hashCode();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object o){
		return this.hashCode() == o.hashCode();
	}
	
	public boolean isIdSet(){
		return this.getId() != null;
	}
}
