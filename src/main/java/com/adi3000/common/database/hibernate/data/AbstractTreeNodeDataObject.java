package com.adi3000.common.database.hibernate.data;

import org.hibernate.Hibernate;

import com.adi3000.common.util.tree.TreeNode;

public abstract class AbstractTreeNodeDataObject<T extends AbstractTreeNodeDataObject<T>> extends AbstractDataObject implements TreeNode<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1304310965177520240L;
	
	public void initializeRecursively(){
		Hibernate.initialize(getChildren());
		for(T child : getChildren()){
			child.initializeRecursively();
		}
	}
}
