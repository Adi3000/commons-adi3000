package com.adi3000.common.database.hibernate.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.database.hibernate.data.DataObject;

public abstract class AbstractDAO<T extends DataObject> extends DatabaseSession{
	

	public AbstractDAO(DatabaseSession db){
		super(db);
	}
	
	public AbstractDAO(){
		super();
	}
	public AbstractDAO(boolean initDbSession){
		super(initDbSession);
	}
	
	public T getDataObjectById(Integer id, Class<? extends DataObject> clazz){
		@SuppressWarnings("unchecked")
		T data = (T)getSession().get(clazz, id);
		return data ;
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
		if(sendForCommit()){
			return data;
		}else{
			return null;
		}
	}
	public T  modifyDataObject(T data){
		modify(data);
		if(sendForCommit()){
			return data;
		}else{
			return null;
		}
	}
	
	public boolean deleteDataObject(T data){
		data.setDatabaseOperation(DatabaseOperation.DELETE);
		persist(data);
		return sendForCommit();
	}
	
	public T getDataObject(AbstractDataObject model)
	{
		@SuppressWarnings("unchecked")
		T toReturn = (T)getSession().get(model.getClass(), model.getId());

		return toReturn;
	}
}
