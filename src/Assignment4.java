/*
 * Seth Wilson
 * COSC 2336.001
 * 6 November 2017
 * Assignment #4: EZ program interpreter
 *
 * This file contains the user-facing code for executing an EZ program.
 */

// Import File I/O classes for file input.
import java.io.File;

public class Assignment4 {
	
	public static void main(String[] args) {
		
		// Declare an input file and initialize it to the path/name provided as the program's first argument.
		File input = new File(args[0]);
		
		// Declare a new EZ program and initialize it with the provided input file.
		EZ program = new EZ(input);
		
		// Run program. Cross fingers.
		program.run();
	}
}
