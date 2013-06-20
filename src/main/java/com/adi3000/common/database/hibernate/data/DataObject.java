package com.adi3000.common.database.hibernate.data;

import java.io.Serializable;

import com.adi3000.common.database.hibernate.DatabaseOperation;

public interface DataObject extends Serializable{

	public void setDatabaseOperation(DatabaseOperation databaseOperation) ;
	public DatabaseOperation getDatabaseOperation() ;
	public Serializable getId();
}
