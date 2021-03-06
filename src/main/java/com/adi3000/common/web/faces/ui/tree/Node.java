package com.adi3000.common.web.faces.ui.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.adi3000.common.util.tree.TreeNode;

public class Node<T extends TreeNode<T>> {
	private T value;
	private Node<T> parent;
	private LinkedList<Node<T>> children = new LinkedList<Node<T>>();
	private List<Node<T>> parents; 
	private int differenceLevel;
	
	public Node(Node<T> parent, T value) {
		this.value = value;
		this.parent = parent;
		this.parents = new ArrayList<Node<T>>();
		Node<T> nextParent = this.parent;
		do{
			this.parents.add(nextParent);
			if(nextParent != null){
				nextParent = nextParent.parent;
			}
		}while(nextParent != null);
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	
	/**
	 * Recursivly construct nodes with TreeObject
	 * Will add only add children which respond to true with the {@code filter} method
	 * @param list
	 */
	public void addChildren(List<T> list){
		List<T> childrenKids = null;
		Node<T> childrenNode = null;
		for(T element : list){
			if(filter(element)){
				childrenNode = initializaNewChildNode(element);
				childrenKids = element.getChildren();
				if(childrenKids != null && !childrenKids.isEmpty()){
					childrenNode.addChildren(childrenKids);
				}
				this.children.add(childrenNode);
			}
		}
	}
	public T getValue() { 
		return value; 
	}

	public boolean getHasParent() { 
		return parent != null; 
	}
	
	public boolean getHasChild() { 
		return children != null && !children.isEmpty(); 
	}

	public boolean isFirstChild() {
		return parent != null && parent.children.peekFirst() == this;
	}

	public boolean isLastChild() {
		return parent != null && parent.children.peekLast() == this;
	}
	
	/**
	 * @return the differenceWithSiblingLevel
	 */
	public int getDifferenceLevel() {
		return differenceLevel;
	}

	/**
	 * @param differenceWithSiblingLevel the differenceWithSiblingLevel to set
	 */
	private void setDifferenceLevel(int differenceWithSiblingLevel) {
		this.differenceLevel = differenceWithSiblingLevel;
	}

	public void setDifferenceLevelWithNode(Node<T> slibingNode){
		this.setDifferenceLevel(slibingNode.getLevel() - this.getLevel()); 
	}
	
	
	public int getLevel(){
		return this.parents.size();
	}

	public int hashCode(){
		int hash = 17;
		hash *= parents.hashCode();
		hash *= value.hashCode();
		return hash;
	}
	/**
	 * Return true if the both Node have the same list of parents and the same value
	 * @param node
	 * @return
	 */
	public boolean equals(Node<T> node){
		return this.parents.equals(node.parents) && this.value.equals(node.value);
	}
	
	/**
	 * Filter  {@code toBeFiltered} and add it if only filter return true
	 * Filter return always true, override this one to add some filter
	 * @param toBeFiltered
	 * @return
	 */
	protected boolean filter(T toBeFiltered){
		return true;
	}
	
	/**
	 * Return a new node with {@code this} as parent
	 * @param element
	 * @return
	 */
	protected Node<T> initializaNewChildNode(T element){
		return new Node<T>(this,element);
	}
}