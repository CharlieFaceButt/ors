package com.lsc.ors.util.structures.trees;

import com.lsc.ors.util.structures.nodes.AVLTreeNode;
import com.lsc.ors.util.structures.nodes.BinaryTreeNode;

/**
 * An AVL tree is a balanced binary tree that maintain its balance by <br>
 * node's height recording.<br>
 * Details see Chapter 13
 * @author charlieliu
 *
 * @param <D> the data type
 */
public class AVLTree<D> extends StandardBinaryTree<D> {

	private int count = 0;
	public int getCount(){
		return count;
	}
	@Override
	public D Insert(Integer key, D data) {
		count ++;
		if(root == null){
			root = new AVLTreeNode<D>(
					key, data, null, null, null);
			return null;
		}
		return ((AVLTreeNode<D>)root).Insert(key, data); 
	};
	
	@SuppressWarnings("unchecked")
	@Override
	public D Delete(Integer key) {
		System.out.println("delete" + key);
		// TODO Auto-generated method stub
		if(root == null) return null;
		
		/*the node you want to delete*/
		AVLTreeNode<D> node = (AVLTreeNode<D>)(root.Search(key));
		/*node not exist*/
		if(node == null) return null;
		/*help do the deletion*/
		AVLTreeNode<D> delete = null;
		
		/*distinguish whether node has two child, delete itself if it doesn't,
		 * otherwise replace itself with its successor/predecessor and delete
		 *  its successor/predecessor*/
		if(node.left == null || node.right == null)
			delete = node;
		else if(node.getBalance() < 0) delete = (AVLTreeNode<D>)(node.Predecessor());
		else delete = (AVLTreeNode<D>)(node.Successor());
		System.out.println("real delete node " + delete.getKey());
		
		/*get the child of the node that get real deletion*/
		BinaryTreeNode<D> child = null;
		if(delete.left != null) child = delete.left;
		else child = delete.right;
		
		/*when doing the real deletion, link the node's parent and its child*/
		if(child != null) child.parent = delete.parent;
		/*when it is the root you want to delete*/
		if(delete.parent == null) root = child;
		else if(delete == delete.parent.left)
			delete.parent.left = child;
		else delete.parent.right = child;
		
		/*when node has two children, replace itself by its successor*/
		D oldData = null;
		if(delete != node){
			node.setKey(delete.getKey());
			oldData = node.getData();
			node.setData(delete.getData());
		}

		/*restore balance for every node along the way from delete to root*/
		delete = (AVLTreeNode<D>)(delete.parent);
		while(delete != null){
			delete.restoreBalance();
			delete = (AVLTreeNode<D>)(delete.parent);
		}
		return oldData;
	}
	
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return ((AVLTreeNode<D>)root).getHeight();
	}
	
	/**
	 * 
	 * @param index 从1开始计数
	 * @return
	 */
	public D get(Integer index){
		AVLTreeNode<D> node = (AVLTreeNode<D>)root;
		while(node.left != null){
			node = (AVLTreeNode<D>)node.left;
		}
		int i = 1;
		while(i < index){
			i ++;
			if(node.right != null){
				node = (AVLTreeNode<D>)node.right;
			} else if(node.parent != null){
				node = (AVLTreeNode<D>)node.parent;
			} else break;
		}
		return node.getData();
	}
}
