package com.adi3000.common.database.hibernate.data;

import java.io.Serializable;

public interface DataObject extends Serializable{

	public void setDatabaseOperation(int databaseOperation) ;
	public int getDatabaseOperation() ;
	public Serializable getId();
}
