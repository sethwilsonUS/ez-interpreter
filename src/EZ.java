/*
 * Seth Wilson
 * COSC 2336.001
 * 6 November 2017
 * Assignment #4: EZ program interpreter
 *
 * This file contains three classes:
 *
 * - EZ:        The meat and potatoes of the EZ programming language with all its methods, plus I/O and helper methods.
 * - Proc:      A small class to store individual EZ procedures.
 * - ProcList:  A linked list containing an EZ program's procedures facilitating lookup by name.
 *
 * Proc and ProcList are private classes within EZ, since the main client code does not need access to them.
 */

// Import classes for file input/output.
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * EZ class--this is the meat of the EZ programming language, containing the setup and functionality.
 */
public class EZ {
	
	// Declare an array to hold EZ's 26 global variables, one for each capital letter.
	private int[] vars;
	
	// Declare the ProcList which will facilitate procedure lookup.
	private ProcList procs;
	
	// Declare the procedure stack which will be used when procedures are called recursively.
	private Stack<Proc> procStack;
	
	/**
	 * Constructor for an EZ program.
	 *
	 * @param input     a text File containing the EZ program code.
	 */
	public EZ(File input) {
		
		// Initialize the EZ global variables, procedure list, and procedure stack.
		vars = new int[26];
		procs = new ProcList();
		procStack = new Stack<>();
		
		// Parse the input file for execution.
		parse(input);
	}
	
	/**
	 * Parses the input text file into procedures.
	 *
	 * @param input a text File containing the EZ program code.
	 */
	private void parse(File input) {
		
		// Attempt to load the input file into a new Scanner object.
		try {
			Scanner scan = new Scanner(input);
			
			// Create a procedure outside the loop that will take in lines of EZ code.
			Proc currProc = null;
			
			// Process each line of the input file.
			while (scan.hasNextLine()) {
				
				// Set up a String for the current line of EZ code.
				String currLine = scan.nextLine();
				
				// Parse out the command, which is the first four characters in an EZ line.
				String cmd = currLine.substring(0, 4);
				
				// If the command is "proc", we need to create a new procedure.
				if (cmd.equalsIgnoreCase("proc")) {
					
					// The remainder of the EZ line serves as the name of the procedure.
					String procName = currLine.substring(5);
					
					// Initialize a new procedure with specified name and set it as currProc so we can add lines to it.
					currProc = new Proc(procName);
					
					// Also add the new procedure to the procList so we can find it later.
					procs.addProc(currProc);
					
				// All other lines, except "stop", will be added to the current procedure.
				} else if (!cmd.equals("stop")) currProc.addLine(currLine);
			}
			
			// Be a good memory citizen and close the Scanner object.
			scan.close();
			
		// Catch and warn of File Not Found errors.
		} catch (FileNotFoundException e) {
			System.out.println("Requested file not found.");
		}
	}
	
	/**
	 * A simple method to run the EZ program.
	 */
	void run() {
		
		// Since all EZ programs start with the "main" procedure, we just need to call it.
		call("main");
	}
	
	/**
	 * This method parses each EZ procedure and decides how each line should be executed.
	 *
	 * @param s     the name of an EZ procedure.
	 */
	private void call(String s) {
		
		// PUSH the procedure corresponding to the passed name onto the procedure stack.
		procStack.push(procs.getProc(s));
		
		// PEEK at the stack, parse the procedure into an array of Strings so it can be processed line by line.
		String[] procArray = procStack.peek().getLines();
		
		// Process each line of the procedure using a "for each" loop.
		for (String line : procArray) {
			
			// As in the parse method, the first four letters of the line serve as the command.
			String cmd = line.substring(0, 4);
			
			// The remainder of the String serves as arguments to be passed to the appropriate EZ method.
			String args = line.substring(5);
			
			// If the command is "call", recursively call the specified procedure.
			if (cmd.equals("call")) call(args);
			
			// If the command is echo, pass the arguments to the EZ echo method.
			else if (cmd.equals("echo")) echo(args);
			
			// The EZ copy method requires a couple extra steps, since copy takes a character and a String as arguments.
			else if (cmd.equals("copy")) {
				
				// The first character in the arguments will be the global variable which will store the copy result.
				char var = args.charAt(0);
				
				// The remainder of the args String contains the expression to be processed.
				String expression = args.substring(2);
				
				// Pass these to the EZ copy method.
				copy(var, expression);
			}
		}
		
		// POP the procedure from the stack.
		procStack.pop();
	}
	
	/**
	 * The EZ echo method outputs a String. Capital letters are replaced with the integer content of their
	 * corresponding global variable.
	 *
	 * @param s     a raw input String.
	 */
	private void echo(String s) {
		
		// Break the input String into an array of characters so they can be processed one by one.
		char[] chars = s.toCharArray();
		
		// Create a StringBuilder object to hold the processed echo String.
		StringBuilder rv = new StringBuilder();
		
		// Iterate through each character using a "for each" loop.
		for (char ch : chars) {
			
			// If the character is a capital letter, add its variable's contents to the output String.
			if (isCapLetter(ch)) rv.append(vars[charToVar(ch)]);
			
			// Otherwise, just add the character to the output String.
			else rv.append(ch);
		}
		
		// Print the output String.
		System.out.println(rv);
	}
	
	/**
	 * The EZ copy method evaluates an infix expression and stores the result in an EZ global variable.
	 *
	 * @param var           an EZ global variable where the result will be stored
	 * @param expression    an infix expression
	 */
	private void copy(char var, String expression) {
		
		// Convert the provided infix expression to postfix, solve the postfix, and save the result to the appropriate
		// global EZ variable.
		vars[charToVar(var)] = solvePostfix(evaluateInfix(expression));
	}
	
	/**
	 * This method evaluates an infix expression and returns a postfix expression.
	 *
	 * @param expression    a String containing an infix expression
	 * @return              a String with the equivalent postfix expression
	 */
	private String evaluateInfix(String expression) {
		
		// Set up a StringBuilder to hold a postfix expression.
		StringBuilder postfix = new StringBuilder();
		
		// Create a Stack of characters to hold operators for the postfix expression.
		Stack<Character> postfixStack = new Stack<>();
		
		// Convert the infix expression into a character array for processing.
		char[] infix = expression.toCharArray();
		
		// Process each line of the character array using a "for each" loop.
		for (char ch : infix) {
			
			// If the character is a number or variable, add it to the postfix String.
			if (isNumber(ch) || isCapLetter(ch)) postfix.append(ch);
			
			// Otherwise, the character is an operator.
			else {
				
				// If character is '+', pop operators off the postfix stack until it's empty. Append operators to the
				// postfix expression.
				if (ch == '+') while (!postfixStack.isEmpty()) postfix.append(postfixStack.pop());
				
				// If the character is a '*', pop and append operands until we hit a '+' or until stack is empty, then
				// push the '*' onto the stack.
				else if (ch == '*')
					while (!postfixStack.isEmpty() && postfixStack.peek() != '+') postfix.append(postfixStack.pop());
				
				// Push the current '*' onto the stack.
				postfixStack.push(ch);
			}
		}
		
		// Pop all remaining operators and append them to the postfix expression.
		while (!postfixStack.isEmpty()) postfix.append(postfixStack.pop());
		
		// Return the postfix expression.
		return postfix.toString();
	}
	
	/**
	 * This method solves a postfix expression and returns an integer.
	 *
	 * @param postfix   a String containing a postfix expression
	 * @return          an integer containing the expression's solution
	 */
	private int solvePostfix(String postfix) {
		
		// Create an integer stack to hold results of postfix operations.
		Stack<Integer> resultStack = new Stack<>();
		
		// Break the expression into an array of characters for processing.
		char[] pfArray = postfix.toCharArray();
		
		// Process each character with a "for each" loop.
		for (char ch : pfArray) {
			
			// If the character is a number, push it to the result stack.
			if (isNumber(ch)) resultStack.push(Character.getNumericValue(ch));
			
			// If the character is a capital letter, push its EZ variable value onto the result stack.
			else if (isCapLetter(ch)) resultStack.push(vars[charToVar(ch)]);
			
			// Otherwise, the character will either be a '+' or '*'.
			else {
				
				// Pop the last two items from the result stack--order does matter.
				int op2 = resultStack.pop();
				int op1 = resultStack.pop();
				
				// Perform the appropriate operation on the operands.
				if (ch == '+') resultStack.push(op1 + op2);
				else if (ch == '*') resultStack.push(op1 * op2);
			}
		}
		
		// If done right, the expression's result should stand alone on the stack. Pop and return it.
		return resultStack.pop();
	}
	
	/**
	 * Simple method to evaluate whether a character is a capital letter.
	 *
	 * @param ch        an input character
	 * @return          true if the character is a capital letter, false if not
	 */
	private boolean isCapLetter(char ch) {
		
		// Cast the character as an int and check its ASCII value.
		return ((int) ch >= 65 && (int) ch <= 90);
	}
	
	/**
	 * Simple method to evaluate whether a character is a number.
	 *
	 * @param ch        an input character
	 * @return          true if the character is a number, false if not
	 */
	private boolean isNumber(char ch) {
		
		// Cast the character as an int and check its ASCII value.
		return ((int) ch >= 48 && (int) ch <= 57);
	}
	
	/**
	 * Given a capital letter, i.e. an EZ global variable, return its corresponding index in the variables array.
	 *
	 * @param ch        an input character, ideally a capital letter
	 * @return          an integer corresponding to the index in the variable in the array
	 */
	private int charToVar(char ch) {
		
		// Cast the character as an int and check its ASCII value. Subtract 65 from it to get a number between 0 and 25.
		return ((int) ch) - 65;
	}
	
	/**
	 * This class contains a procedure as a String, with commands separated by line breaks. A "name" value is also
	 * provided to facilitate procedure lookup..
	 */
	private class Proc {
		
		// Declare String variables for proc, containing the EZ code, and name, for the procedure's name.
		private String proc;
		private String name;
		
		/**
		 * Proc Constructor initializing a blank procedure String, and taking in the procedure's name as an argument.
		 *
		 * @param n     a String containing the procedure's name
		 */
		Proc(String n) {
			proc = "";
			name = n;
		}
		
		/**
		 * This method adds a line to the procedure, used by EZ's parse method during file import.
		 *
		 * @param s     a String containing a line of EZ code
		 */
		void addLine(@NotNull String s) {
			
			// Add the command followed by a new line character, used to distinguish lines during EZ's call method.
			proc = proc + s + "\n";
		}
		
		/**
		 * Standard getter for the procedure's name variable.
		 *
		 * @return      the procedure's name as a String
		 */
		String getName() { return name; }
		
		/**
		 * Separates the procedure's single String into a String array, providing individual lines of code to EZ's call
		 * method.
		 *
		 * @return      a String array containing individual lines of EZ code
		 */
		String[] getLines() { return proc.split("\n"); }
	}
	
	/**
	 * This class is essentially an unordered linked list of procedures in an EZ program. Its primary purpose is to
	 * allow the lookup of individual procedures by name in EZ's parse method for adding lines to a procedure, and in
	 * EZ's call method for executing a procedure.
	 */
	private class ProcList {
		
		// Declare head as the first procedure-holding Node in the linked list.
		private Node<Proc> head;
		
		/**
		 * ProcList constructor, initializing head to null.
		 */
		ProcList() { head = null; }
		
		/**
		 * This method, used by EZ's parse method in building the program, adds a new procedure to the list.
		 *
		 * @param p     a Proc object passed in from EZ's parse method..
		 */
		void addProc(Proc p) {
			
			// Declare and initialize a new Node; the Node's Proc is initialized by parse with the appropriate name.
			Node<Proc> newProc = new Node<>(p);
			
			// If head is empty, set head as the new Node.
			if (head == null) head = newProc;
			
			// Otherwise, iterate to the end of the linked list and attach the Node there.
			else {
				Node<Proc> curr = head;
				while (curr.getNext() != null) curr = curr.getNext();
				curr.setNext(newProc);
			}
		}
		
		/**
		 * This method allows EZ's parse and call methods to find a procedure by name to add lines or execute code.
		 *
		 * NOTE: This method assumes only valid procedure names will be passed to it--it isn't set up to handle errors.
		 *
		 * @param s     a String containing the name of a procedure
		 * @return      the Proc object with the corresponding name
		 */
		Proc getProc(String s) {
			
			// Declare the Proc to be returned and initialize it to null.
			Proc proc = null;
			
			// Declare curr as a new Proc-holding Node and initialize it to head.
			Node<Proc> curr = head;
			
			// Declare and initialize a boolean used to break the while loop.
			boolean foundProc = false;
			
			// Iterate through the list until the Proc with the supplied name is found, or until the end of the list.
			while (curr != null && !foundProc) {
				
				// If the Proc with the specified name is found, set it as the returned Proc and break the loop.
				if (curr.getItem().getName().equals(s)) {
					proc = curr.getItem();
					foundProc = true;
				}
				
				// Iterate through the loop.
				curr = curr.getNext();
			}
			
			// Return the procedure with the corresponding name.
			return proc;
		}
	}
}