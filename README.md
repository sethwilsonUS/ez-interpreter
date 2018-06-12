# EZ Interpreter

This simple command interpreter was a project for my Data Structures course. It was intended to demonstrate our knowledge of stacks, recursion, and infix/postfix algorithms. It also demonstrates my coding and commenting style. Because it's a class project, comments are more verbose than they might otherwise be--know your audience--though I do tend to comment quite a bit.

## Introduction

Given an input text file with a set of commands, this program will run the commands and produce the output--like a barebones assembler. See the PDF for the original assignment.

## Commands

The following are valid EZ commands.

- `proc <procedure name>`: This is how procedures are defined. One procedure must be called `main`; this is the procedure that will run the program. Note that though procedure calls can be nested, procedure definitions cannot. Each `proc` ends the previous procedure and starts a new one.
- `call <procedure name>`: This command calls defined procedures. Call commands can be nested. The first procedure to be called will always be `main`.
- `copy <variable> <expression>`: EZ variables correspond to capital letters A-Z, for a total of 26 variables. Expressions are limited to a single integer value, integer addition, or integer multiplication. Variables can also be used in expressions. Examples: `copy A 2`, `copy B 3`, `copy C A+B`, `copy D B*C`.
- `echo <string>`: Echo a string, with capital letters replaced with variables. So, to display the value of A, you'd have to use `echo a is A`. I haven't yet implemented escape characters to allow for capital letters.

## TO DO

- Implement additional mathematical functions in `copy`.
- Implement escape characters in `echo` command. 