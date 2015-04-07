package com.lsc.ors.util.structures.nodes;



/**
 * An AVL tree node is also a binary tree that balanced by keep record of the height<br>
 * of the tree's branch from the node
 * @author charlieliu
 *
 * @param <D> the data type
 */
public class AVLTreeNode<D> extends BinaryTreeNode<D> {

	private static final int RR = 12;
	private static final int RL = 9;
	private static final int LR = 6;
	private static final int LL = 3;
	private static final int B = 0;
	
	/**
	 * record of tree's height from this node
	 */
	private int height = 0;
	
	/**
	 * record of the amount of left nodes
	 */
	private int leftCount = 0;
	
	public int getHeight() {
		return height;
	}

	/**
	 * record of the difference between two branches' height
	 */
	private int balance = 0;
	
	public void update(){
		int r = 0,l = 0;
		if(right != null) r = ((AVLTreeNode<D>)right).getHeight();
		if(left != null) l = ((AVLTreeNode<D>)left).getHeight();
		balance = r - l;
		height = (r > l) ? (r + 1) : (l + 1);
	}
	
	public AVLTreeNode(Integer key, D data, BinaryTreeNode<D> left,
			BinaryTreeNode<D> right, BinaryTreeNode<D> parent) {
		super(key, data, left, right, parent);
		// TODO Auto-generated constructor stub
		this.height = 1;
	}
	
	public void LeftRotate(){
		if(right == null) return;
		
		AVLTreeNode<D> rightNode = (AVLTreeNode<D>)(this.right);
		
		/*links between rightNode and this*/
		rightNode.parent = this.parent;
		this.right = rightNode.left;
		
		/*links between this and parent*/
		if(this.parent != null){
			if(this.parent.left == this) this.parent.left = rightNode;
			else this.parent.right = rightNode;
		}
		this.parent = rightNode;
		
		/*links between rightNode.left and rightNode*/
		if(rightNode.left != null) rightNode.left.parent = this;
		rightNode.left = this;
		
		this.update();
		rightNode.update();
		rightNode = ((AVLTreeNode<D>)(rightNode.parent));
		if(rightNode != null) rightNode.update();
	}
	
	public void RightRotate(){
		if(left == null) return;
		
		AVLTreeNode<D> leftNode = (AVLTreeNode<D>)(this.left);
		
		/*links between leftNode and this*/
		leftNode.parent = this.parent;
		this.left = leftNode.right;
		
		/*links between this and parent*/
		if(this.parent != null){
			if(this.parent.left == this) this.parent.left = leftNode;
			else this.parent.right = leftNode;
		}
		this.parent = leftNode;
		
		/*links between leftNode.right and leftNode*/
		if(leftNode.right != null) leftNode.right.parent = this;
		leftNode.right = this;
		
		this.update();
		leftNode.update();
		leftNode = ((AVLTreeNode<D>)(leftNode.parent));
		if(leftNode != null) leftNode.update();
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getLeftCount() {
		return leftCount;
	}
	
	public void setLeftCount(int count) {
		this.leftCount = count;
	}
	
	public D Insert(Integer key, D data){
		
		D oldData = null;
		if(key > this.key){
			if(right == null){
				this.right = new AVLTreeNode<D>(
						key, data, null, null, this);
				balance ++;
				height = 2;
			}
			else{
				oldData = ((AVLTreeNode<D>)(this.right)).Insert(key, data);
				callRotation(balanceMode());
			}
		}
		else if(key < this.key){
			if(left == null){
				this.left = new AVLTreeNode<D>(
						key, data, null, null, this);
				balance --;
				height = 2;
			}
			else{
				oldData = ((AVLTreeNode<D>)(this.left)).Insert(key, data);
				callRotation(balanceMode());
			}
		}
		else{
			oldData = this.data;
			this.data = data;
			return oldData;
		}
		return oldData;
	}
	
	/**
	 * Do the rotation base on the balance mode. Rotation strategies are <br>
	 * realized as below:<br>
	 *  - RR: left rotate
	 *  - RL: right child do right rotation, then this node left rotate
	 *  - LL: right rotate
	 *  - LR: left child do left rotation, then this node right rotate
	 * @param bMode
	 */
	private void callRotation(int bMode){
		switch(bMode){
		case RL:((AVLTreeNode<D>)this.right).RightRotate();
		case RR:LeftRotate();break;
		case LR:((AVLTreeNode<D>)this.left).LeftRotate();
		case LL:RightRotate();break;
		default:break;
		}
	}
	
	/**
	 * Get the state of balance of this node. The state has the following<br>
	 * values:<br>
	 * 	- B: balanced, the tree is balanced<br>
	 *  - RR: this node has balance of +2, and its right child has balance of +1<br>
	 *  - RL: this node has balance of +2, and its right child has balance of -1<br>
	 *  - LR: this node has balance of -2, and its right child has balance of +1<br>
	 *  - LL: this node has balance of -2, and its right child has balance of -1<br>
	 * @return the state
	 */
	private int balanceMode(){
		update();
		int r = 0,l = 0;
		if(right != null) r = ((AVLTreeNode<D>)right).getHeight();
		if(left != null) l = ((AVLTreeNode<D>)left).getHeight();
		int bl = r - l;
		if(bl == 2){
			if(((AVLTreeNode<D>)right).getBalance() == -1) return RL;
			else return RR;
		}
		else if(bl == -2){
			if(((AVLTreeNode<D>)left).getBalance() == 1) return LR;
			else return LL;
		}
		else return B;
	}

	/**
	 * restore the balance of this node
	 */
	public void restoreBalance(){
		callRotation(balanceMode());
		System.out.println("restore balance of " + key + "to " + balance);
	}
	
}
