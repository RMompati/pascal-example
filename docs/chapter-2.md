# Framework I: Compiler and Interpreter

> The goal is to build a flexible framework that can support both a compiler and an interpreter.

## Goals and Approach

> #### The goals:
>
>   - A source language-independent framework that supports both compilers and interpreters.
>   - Initial Pascal source language-specific components integrated into the front end of the framework.
>   - Initial compiler and interpreter components integrated into the backend of the framework.
>   - Simple end-to-end runs that exercise the components by generating source program listings from the common frontend
> and messages from the compiler or interpreter backend.

### The noted design patterns
>   - The Observer design pattern
>     - #### The classes and interfaces.
>       - MessageProducer interface
>       - MessageListener interface
>       - MessageHandler class
