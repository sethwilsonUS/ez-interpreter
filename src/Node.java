/*
 * Seth Wilson
 * COSC 2336.001
 * 6 November 2017
 * Assignment #4: EZ program interpreter
 *
 * This file contains a slightly modified version of the Node class we wrote in class. This Node class uses generics,
 * since it's used to store lots of different kinds of data.
 */

/**
 * A generics-modified Node class.
 *
 * @param <T>       an Object type determining the kind of data stored in the Node.
 */
public class Node<T> {
	
	// Declare the object item and the next Node pointer.
	private T item;
	private Node<T> next;
	
	/**
	 * Node constructor taking in an object of type T--no need for a default constructor here.
	 *
	 * @param x     an Object corresponding to the Node's type
	 */
	public Node(T x)
	{
		
		// Set the passed Object parameter as the node's content.
		item = x;
		
		// Set the next Node<T> to null.
		next = null;
	}
	
	/**
	 * Standard getter, returning the Node's contents of type T.
	 *
	 * @return      an Object of type T
	 */
	public T getItem() { return item; }
	
	// No need for setItem() in this implementation.
	
	/**
	 * Get the next Node<T> in a list.
	 *
	 * @return      the next Node<T> object in a list.
	 */
	public Node<T> getNext()
	{
		return next;
	}
	
	/**
	 * Set the next Node<T> in a list--used for adding and removing Nodes.
	 *
	 * @param n     a Node of type T to serve as this Node's next
	 */
	public void setNext(Node<T> n) { next = n; }
}