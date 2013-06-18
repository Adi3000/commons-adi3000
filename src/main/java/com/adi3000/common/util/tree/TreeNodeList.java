package com.adi3000.common.util.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adi3000.common.web.faces.ui.tree.Node;

public class TreeNodeList<T extends TreeNode<T>> implements Serializable {

	private Node<T> rootNode;
	private List<Node<T>> orderedNodelist;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7536347740078406017L;
	public TreeNodeList(T root){
		super();
		this.rootNode =  new Node<T>(null,root);
	}
	
	private void walk(Node<T> node){
		orderedNodelist.add(node);
		Node<T> previousNode = null;
		for(int i=0; i < node.getChildren().size(); i ++){
			Node<T> kid = node.getChildren().get(i);
			if(orderedNodelist.size() - 1 >= 0 ){
				previousNode = orderedNodelist.get(orderedNodelist.size() - 1 );
			}else{
				previousNode = rootNode;
			}
			kid.setDifferenceLevelWithNode(previousNode);
			walk(kid);
		}
	}
	
	/**
	 * Wipe old list and get a new list with the root node
	 * @return
	 */
	public List<Node<T>> getTreeNodeList(){
		this.orderedNodelist = new ArrayList<Node<T>>();
		walk(rootNode);
		return orderedNodelist;
	}
	
	public Node<T> getRootNode(){
		return rootNode;
	}
}
