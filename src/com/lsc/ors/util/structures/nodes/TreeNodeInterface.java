package com.lsc.ors.util.structures.nodes;

public interface TreeNodeInterface<K>{

	/**
	 * Get the parent of this node
	 * @return
	 */
	public abstract TreeNodeInterface<K> GetParent();

	/**
	 * From this node, search the descendants by key 
	 * @param key
	 * @return the node match the key; null if such node not exist
	 */
	public abstract TreeNodeInterface<K> Search(K key);
	
	/**
	 * Iteration from left
	 * @return
	 */
	public abstract String OrderedTreeWalk();
	
	/**
	 * From this node, get the minimum key value among descendants and itself
	 * @return
	 */
	public abstract TreeNodeInterface<K> Minimum();
	
	/**
	 * From this node, get the maximum key value among descendants and itself
	 * @return
	 */
	public abstract TreeNodeInterface<K> Maximum();
	
	/**
	 * Get the node that has the successive value of this node
	 * @return
	 */
	public abstract TreeNodeInterface<K> Successor();
	
	/**
	 * Get the predecessor node of this node by key value
	 * @return
	 */
	public abstract TreeNodeInterface<K> Predecessor();
}
