package com.adi3000.common.util.tree;

import java.util.List;

public interface TreeNode<T> {
	/**
	 * Method to retrieve all children of this object
	 * @return list of children
	 */
	public List<T> getChildren();
	/**
	 * Return the parent of this object
	 * @return
	 */
	public T getAncestor();
}
