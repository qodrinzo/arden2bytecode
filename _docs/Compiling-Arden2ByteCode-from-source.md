---
# GENERATED - DO NOT EDIT
title: Compiling Arden2ByteCode from source
---
## Getting the source
To use [Arden2ByteCode](https://github.com/PLRI/arden2bytecode), first download or clone the source.

You can use `git clone` as follows:  
`git clone git://github.com/PLRI/arden2bytecode.git`

Alternatively, you can download a zipball or tarball from the following URLs:  
Zipball: [https://github.com/PLRI/arden2bytecode/zipball/master](https://github.com/PLRI/arden2bytecode/zipball/master)  
Tarball: [https://github.com/PLRI/arden2bytecode/tarball/master](https://github.com/PLRI/arden2bytecode/tarball/master)

Clone or extract the files to a directory of your choice.

## Building the source
Now that you got the source, you can build (compile) it. There are 2 different ways to build it:

1. **Using [Eclipse](http://www.eclipse.org/) (recommended)**  
   If you want to use Eclipse, import the Eclipse project coming with the source code.  
   1. Choose **File -> Import...** from the Eclipse menu.
   1. In the import dialog choose "Existing Projects into Workspace" and click "Next"
   1. As root directory, choose the directory where you put the source tree and click "Finish"
   1. Now the project arden2bytecode should be imported and opened on the left side of your Eclipse IDE window (in the Package Explorer).
   1. Build the project by choosing **Project -> Build Project...** from the Eclipse menu.
1. **Using [Apache Ant](http://ant.apache.org/)**  
   Ant is a command-line utility mainly used to compile Java programs like arden2bytecode.  
   1. First of all, you need to have [ant](http://ant.apache.org/) installed and the binary being in the path of your environment.
   1. Further, you need a [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) of at least version 6 to be able to compile the Java Sources.
   1. Using a shell (command line interpreter), switch to the directory containing the source code (build.xml should be located in that directory).
   1. Type `ant`, to start the compile procedure.

The compiled Java program should now reside in subdirectory `bin/` of the source tree.  
Also, a JAR package of all the binaries should be built in the 'dist/' subdirectory.

## Launching Arden2ByteCode
Having built the source, you can now launch the Arden2ByteCode compiler.

1. Do this by switching to the `bin/` subdirectory of the source tree.
1. Enter `java arden.MainClass -r <Path to MLM-File>` on the command prompt.  
   E.g. `java arden.MainClass -r ../src/arden/tests/x2.1.mlm`

### Alternatively: Launching the .jar file
You can also launch the .jar binary package as explained in [[Getting started with Arden2ByteCode]].

1. Switch to `dist/` subdirectory: `cd dist`
1. Enter `arden2bytecode -r ../src/arden/tests/x2.5.mlm` on the command prompt to run `x2.5.mlm`.  
   On Linux, type `./arden2bytecode -r ../src/arden/tests/x2.5.mlm`.

More information about the command line options are given in [[Arden2ByteCode Command Line Reference]].
