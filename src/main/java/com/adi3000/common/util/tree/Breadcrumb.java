package com.adi3000.common.util.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Breadcrumb<T extends TreeNode<T>> {
	private List<T> breadcrumb;
	
	public Breadcrumb(T edge){
		breadcrumb = new ArrayList<T>();
		T currentKeyword = edge;
		while(currentKeyword != null){
			breadcrumb.add(currentKeyword);
			currentKeyword = currentKeyword.getAncestor();
		}
		Collections.reverse(breadcrumb);
	}
	
	public List<T> getBreadcrumb(){
		return breadcrumb;
	}
}
