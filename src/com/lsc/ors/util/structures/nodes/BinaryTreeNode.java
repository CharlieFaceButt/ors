package com.lsc.ors.util.structures.nodes;

import com.lsc.ors.util.structures.KeyTaggedDataBean;

/**
 * Node of BinaryTree
 * A binary tree node package the (key = Integer,data) pair with a parent link<br>
 * and children node of left and right
 * @author charlieliu
 *
 * @param <D> the data type
 */
public class BinaryTreeNode<D> extends KeyTaggedDataBean<Integer, D> implements TreeNodeInterface<Integer>{

	public BinaryTreeNode<D> left;
	public BinaryTreeNode<D> right;
	public BinaryTreeNode<D> parent;
	
	public BinaryTreeNode(
			Integer key, 
			D data,
			BinaryTreeNode<D> left,
			BinaryTreeNode<D> right,
			BinaryTreeNode<D> parent) {
		super(key, data);
		this.left = left;
		this.right = right;
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public TreeNodeInterface<Integer> GetParent() {
		// TODO Auto-generated method stub
		return parent;
	}


	@Override
	public TreeNodeInterface<Integer> Search(Integer key) {
		// TODO Auto-generated method stub
		if(this.key == key) return this;
		if(this.key < key && this.right != null) return right.Search(key);
		if(this.key > key && this.left != null) return left.Search(key);
		return null;
	}

	@Override
	public String OrderedTreeWalk() {
		// TODO Auto-generated method stub
		String result = "";
		if(this.left != null) result += left.OrderedTreeWalk();
		result += (this.key + ";");
		if(this.right != null) result += right.OrderedTreeWalk();
		return result;
	}

	@Override
	public TreeNodeInterface<Integer> Minimum() {
		// TODO Auto-generated method stub
		BinaryTreeNode<D> node = this;
		while(node.left != null) node = node.left;
		return node;
	}

	@Override
	public TreeNodeInterface<Integer> Maximum() {
		// TODO Auto-generated method stub
		BinaryTreeNode<D> node = this;
		while(node.right != null) node = node.right;
		return node;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeNodeInterface<Integer> Successor() {
		// TODO Auto-generated method stub
		if(right != null) return right.Minimum();
		
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)Search(key);
		BinaryTreeNode<D> parent = (BinaryTreeNode<D>)node.GetParent();
		while(parent != null && parent.right == node){
			node = parent;
			parent = (BinaryTreeNode<D>)node.GetParent();
		}
		return parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeNodeInterface<Integer> Predecessor() {
		// TODO Auto-generated method stub
		
		if(left != null) return left.Minimum();
		
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)Search(key);
		BinaryTreeNode<D> parent = (BinaryTreeNode<D>)node.GetParent();
		while(parent != null && parent.left == node){
			node = parent;
			parent = (BinaryTreeNode<D>)node.GetParent();
		}
		return parent;
	}
}
