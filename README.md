Arden2ByteCode - Compiler for Arden Syntax with Java Bytecode output
====================================================================


## Usage
Check out the [wiki](https://github.com/PLRI/arden2bytecode/wiki) to see how to [install and start using](https://github.com/PLRI/arden2bytecode/wiki/Installation) Arden2ByteCode, learn more about [Arden Syntax](https://github.com/PLRI/arden2bytecode/wiki/Basics) or for a list of [command line options](https://github.com/PLRI/arden2bytecode/wiki/Command-Line-Options).

Compile an MLM:
```sh
arden2bytecode -c hello_world.mlm
```

Compile and run an MLM:
```sh
arden2bytecode -r hello_world.mlm
```


## Building
To compile Arden2ByteCode, you first need to generate the parser using the SableCC parser generator.

To build the parser with **Eclipse**, import the project and choose *Project* &rArr; *Build project* from the menu. On the first build you need to refresh the Project (F5), so Eclipse finds the downloaded [dependencies](#dependencies).  

To build with **Ant**, `cd` into the project root and type `ant` at the command prompt.  

Both will automatically call the "sableCC" target in the [build.xml](build.xml) and generate the .java files for the parser. This can take some time, but will only happen if the parser does not exist or is out-of-date.

Check out the wiki for [detailed instructions](https://github.com/PLRI/arden2bytecode/wiki/Building).


## Dependencies
The dependencies are downloaded automatically if you use [Ant](https://ant.apache.org/) or [Eclipse](https://eclipse.org/) to build the project. See the "dependencies" target in [build.xml](build.xml) for download links.

- [SableCC](http://www.sablecc.org/): A "compiler-compiler" which is used to generate the arden parser from a grammar file. Usage: `java -jar tools/sablecc.jar -d src/ src/arden.scc src/ardenConstants.scc`
- [JewelCli](http://jewelcli.lexicalscope.com/): A command line arguments parser.
- [JUnit](http://junit.org/): A testing framework to test the correct implementation and standard compliance.
- [Hamcrest-core](http://hamcrest.org/JavaHamcrest/): Used with JUnit to create short and concise tests.


## Standard Conformance
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


## Copyright and license
- Copyright 2004: University of British Columbia
- Copyright 2009-2010: Daniel Grunwald
- Copyright 2011-2012: Hannes Flicka
Portions (arden.scc)

See [LICENSE.md](LICENSE.md) for licensing information.
