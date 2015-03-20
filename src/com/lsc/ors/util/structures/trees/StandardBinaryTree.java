package com.lsc.ors.util.structures.trees;

import com.lsc.ors.util.structures.nodes.BinaryTreeNode;

/**
 * StandardBinaryTree provides access to only one binary tree, these access <br>
 * interfaces are:<br>
 *  - Minimum/Maximum
 *  - Predecessor/Successor
 *  - Insert/Delete
 *  - OrderedTreeWalk
 *  - getHeight
 *  - getAmount 
 * @author charlieliu
 *
 * @param <D> the data type
 */
public class StandardBinaryTree<D> implements SearchTreeInterface<Integer, D> {

	protected BinaryTreeNode<D> root = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public D Search(Integer key) {
		// TODO Auto-generated method stub
		if(root == null) return null;
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)(root.Search(key));
		if(node == null) return null;
		else return node.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer Minimum() {
		// TODO Auto-generated method stub
		if(root == null){
			System.out.println("StandardBinaryTree: can not get " +
				"key values because the tree has not been established");
			return null;
		}
		return ((BinaryTreeNode<D>)(root.Minimum())).getKey();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer Maximum(){
		// TODO Auto-generated method stub
		if(root == null){
			System.out.println("StandardBinaryTree: can not get " +
				"key values because the tree has not been established");
			return null;
		}
		return ((BinaryTreeNode<D>)(root.Maximum())).getKey();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer Predecessor(Integer key) {
		// TODO Auto-generated method stub
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)(root.Search(key));
		if(node == null) return null;
		node = (BinaryTreeNode<D>)(node.Predecessor());
		if(node == null) return null;
		else return node.getKey();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer Successor(Integer key) {
		// TODO Auto-generated method stub
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)(root.Search(key));
		if(node == null) return null;
		node = (BinaryTreeNode<D>)(node.Successor());
		if(node == null) return null;
		else return node.getKey();
	}

	@Override
	public D Insert(Integer key, D data) {
		// TODO Auto-generated method stub
		BinaryTreeNode<D> node = root;
		BinaryTreeNode<D> parent = null;
		while(node != null){
			parent = node;
			if(key < node.getKey()) node = node.left;
			else if(key > node.getKey()) node = node.right;
			else{
				D oldData = node.getData();
				node.setData(data);
				return oldData;
			}
		}
		BinaryTreeNode<D> insert = new BinaryTreeNode<D>(key, data, null, null, parent);
		if(parent == null) root = insert;
		else if(key < parent.getKey()) parent.left = insert;
		else parent.right = insert;
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public D Delete(Integer key) {
		// TODO Auto-generated method stub
		if(root == null) return null;
		
		/*the node you want to delete*/
		BinaryTreeNode<D> node = (BinaryTreeNode<D>)(root.Search(key));
		/*node not exist*/
		if(node == null) return null;
		/*help do the deletion*/
		BinaryTreeNode<D> delete = null;
		
		/*distinguish whether node has two child, delete itself if it doesn't,
		 * otherwise replace itself with its successor and delete its successor*/
		if(node.left == null || node.right == null)
			delete = node;
		else delete = (BinaryTreeNode<D>)(node.Successor());
		
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
		return oldData;
	}

	@Override
	public String OrderedTreeWalk() {
		// TODO Auto-generated method stub
		return root.OrderedTreeWalk();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return getHeight(root);
	}
	private int getHeight(BinaryTreeNode<D> node){
		if(node == null) return 0;
		int hleft = getHeight(node.left);
		int hright = getHeight(node.right);
		if(hleft > hright) return hleft + 1;
		else return hright + 1;
	}

	@Override
	public int getAmount() {
		// TODO Auto-generated method stub
		return countInAmount(root);
	}
	private int countInAmount(BinaryTreeNode<D> node){
		if(node == null) return 0;
		return countInAmount(node.left) + 1 + countInAmount(node.right);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = super.toString();
		result += "\n - height: " + getHeight();
		result += "\n - number of elements: " + getAmount();
		result += "\n - key range from " + Minimum() + " to " + Maximum();
		return result;
	}
}
