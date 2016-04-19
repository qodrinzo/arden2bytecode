Arden2ByteCode - Compiler for Arden Syntax with Java Bytecode output
====================================================================

Copyright 2009-2010, Daniel Grunwald , 2011-2012, Hannes Flicka
Portions (arden.scc) Copyright 2004, University of British Columbia  
See LICENSE.md for licensing information.


Dependencies
------------

The dependencies are downloaded automatically if you use [Ant](https://ant.apache.org/) or [Eclipse](https://eclipse.org/) to build the project. See the "dependencies" target in [build.xml](build.xml) for download links.

- [SableCC](http://www.sablecc.org/): A "compiler-compiler" which is used to generate the arden parser from a grammar file. Usage: `java -jar tools/sablecc.jar -d src/ src/arden.scc src/ardenConstants.scc`
- [JewelCli](http://jewelcli.lexicalscope.com/): A command line arguments parser.
- [JUnit](http://junit.org/): A testing framework to test the correct implementation and standard compliance.
- [Hamcrest-core](http://hamcrest.org/JavaHamcrest/): Used with JUnit to create short and concise tests.


Building
--------
To compile Arden2ByteCode, you first need to generate the parser using the SableCC parser generator.

To build the parser with **Eclipse**, import the project and choose *Project* &rArr; *Build project* from the menu. On the first build you need to refresh the Project (F5), so Eclipse finds the downloaded dependencies.  
To build with **Ant**, `cd` into the project root and type `ant` at the command prompt.  

Both will automatically call the "sableCC" target in the [build.xml](build.xml) and generate the .java files for the parser in the packages `analysis`, `lexer`, `node`, `parser`. This can take some time, but will only happen if the .java files do not exist or are out-of-date.

When the input grammar is changed, you will need to regenerate the parser. Before regenerating the parser, you should call the "clean" target (*Project* &rArr; *Clean&hellip;*) to ensure there aren't any old files left behind.


Usage
-----

This is explained in detail in the wiki: [Getting started with Arden2ByteCode](https://github.com/PLRI/arden2bytecode/wiki/Getting-started-with-Arden2ByteCode)


Notes to the Present Implementation
-----------------------------------

Daniel:  
I believe this compiler fully implements Arden Syntax 2.5 with the following exceptions:

Languages features not implemented:

* From Arden Syntax 2.1 specification:
    * 10.2.4.6 Event Call
    * 11.2.2 Event Statement
    * 13 Evoke Slot
* From Arden Syntax 2.5 specification:
    * 11.2.5.2 Message As statement
    * 11.2.5.6 Destination As statement
    * Some string formatting specificiers are not implemented.
    * There is no way to use Arden variables within mapping clauses.
    * Citation/links slots are not syntax checked.
    * The compiler does not check that no languages features newer than the specified 'Arden Version' are used.
