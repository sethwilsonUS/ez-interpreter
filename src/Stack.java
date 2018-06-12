/*
 * Seth Wilson
 * COSC 2336.001
 * 6 November 2017
 * Assignment #4: EZ program interpreter
 *
 * This file contains a slightly modified version of the reference-based Stack class we wrote in class. This Stack
 * class uses generics, since it's used to store lots of different kinds of data.
 */

/**
 * A generics-modified Stack class.
 *
 * @param <T>       an Object type determining the kind of data stored in the Stack.
 */
public class Stack<T> {
	
	// Declare a Node of type T pointing to the top of the Stack.
	private Node<T> top;
	
	/**
	 * Stack constructor setting top to null.
	 */
	public Stack() {
		top = null;
	}
	
	/**
	 * A method for determining whether the Stack is empty--just check whether top equals null.
	 *
	 * @return      a boolean, true if the Stack is empty, false if not
	 */
	public boolean isEmpty() {
		return (top == null);
	}
	
	/**
	 * A method for pushing an Object of type T onto the Stack.
	 *
	 * @param x     an Object of type T to be added to the top of the Stack
	 */
	public void push(T x) {
		
		// Declare a new Node to be pushed onto the Stack; initialize the Node with the Object to be added.
		Node<T> pushedNode = new Node<>(x);
		
		// Point the new Node to the current top . . .
		pushedNode.setNext(top);
		
		// . . . and then make top the new Node.
		top = pushedNode;
	}
	
	/**
	 * Pop the top Object from the Stack, and return it in case the client needs it.
	 *
	 * @return      an Object of type T--the contents of the popped Node from the Stack
	 */
	public T pop() {
		
		// Declare a variable to hold the popped Object; initialize it to null.
		T poppedItem = null;
		
		// If the Stack is empty, there ain't nothin' to pop.
		if (isEmpty()) System.out.println("Stack is empty! Nothing to pop!");
		
		// Otherwise, get the Object from top, and then point top to the next item in the Stack.
		else {
			poppedItem = top.getItem();
			top = top.getNext();
		}
		
		// Return the popped Object.
		return poppedItem;
	}
	
	/**
	 * Peek at the top of the Stack and return the Object found there for client use--like pop, but nothing is removed.
	 *
	 * @return      an Object of type T found at the top of the Stack
	 */
	public T peek() {
		
		// Declare the Object to be returned; initialize it to null.
		T peekedItem = null;
		
		// If the Stack is empty, ain't nothin' to peek at.
		if (isEmpty()) System.out.println("Stack is empty! Nothing to peek at.");
		
		// Otherwise, get the Object off the top of the Stack.
		else peekedItem = top.getItem();
		
		// Return the Object.
		return peekedItem;
	}
}