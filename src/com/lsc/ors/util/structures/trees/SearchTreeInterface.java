package com.lsc.ors.util.structures.trees;

public interface SearchTreeInterface<K,D> {

	/**
	 * Search the element that has the same key
	 * @param key
	 * @return the element Data; null if no element matches
	 */
	public abstract D Search(K key);
	
	/**
	 * Get the minimum key value
	 * @return
	 */
	public abstract K Minimum();
	
	/**
	 * Get the maximum key value
	 * @return
	 */
	public abstract K Maximum();
	
	/**
	 * Get the next key, which means the result key value has the smallest value that bigger than the 
	 * given one
	 * @param key
	 * @return the next key
	 */
	public abstract K Predecessor(K key);
	
	/**
	 * Get the previous key, which means the result key value has the largest value that smaller than
	 * the given one
	 * @param key
	 * @return the previous key
	 */
	public abstract K Successor(K key);
	
	/**
	 * Insert a data to the position of a key value, it will replace the original data if the key is 
	 * already occupied
	 * @param key
	 * @param data
	 * @return the original data of the key; null if nothing has been occupying the key
	 */
	public abstract D Insert(K key, D data);
	
	/**
	 * Delete the element that has the same key
	 * @param key
	 * @return the original data
	 */
	public abstract D Delete(K key);
	
	/**
	 * Iteration
	 * @return the list of keys
	 */
	public abstract String OrderedTreeWalk();
	
	/**
	 * Get the height of the tree
	 * @return the height
	 */
	public abstract int getHeight();
	
	/**
	 * Get the amount of elements that stored in the tree
	 * @return the amount
	 */
	public abstract int getAmount();
}
