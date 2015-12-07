---
# GENERATED - DO NOT EDIT
title: Arden2ByteCode Command Line Reference
---
### Synopsis
`arden2bytecode [OPTIONS] [FILE] [FILE...]`

### Options
* **-h**, **-?**, **--help**  
  Display help to command line options.
* **-c**, **--compile**  
  Compile the input files (must be of .mlm type) and write output of compiler to .class files.
* **-o** FILE, **--output** FILE  
  Output .class file to write compiled MLM to. This is intended to be used in conjunction with **-c**.
* **-r**, **--run**  
  Run the input files directly. The input files must be either .mlm files which will be compiled in-memory or already compiled .class MLMs.
* **-v**, **--verbose**  
  Verbose output mode.  
  For example the input files will be explicitly listed in this mode.
* **-n**, **--nologo**  
  Don't display the header with program name and licensing information.
* **-a**, **--arguments**  
  Specify arguments to the MLM. These are available in the MLM 
  data slot via the `argument` identifier.  
  The parameter after the **-a** flag must be a valid Arden Syntax
  expression, such as `("arg 1", "arg 2", 34.5)`.  
  As you can see, strings must be surrounded by quotes which must
  be properly escaped by your command line interpreter when being
  passed to Arden2ByteCode.  
  Example: `$ arden2bytecode -a "(\"arg 1\", \"arg 2\", 34.5)" -r mlmname.mlm`
  when using Windows or Bash shell syntax.  
  Also note that you may pass multiple arguments to the **-a** flag,
  which are then treated as lists and concatenated.
* **--daemon**  
  Starts a daemon that invokes the MLMs given as input files on the 
  schedule given in the respective evoke slots.  
  This option replaces **-r** or **-c**.

**Setting up the MLM environment/context**

* **-p**, **--classpath**  
  An additional classpath from where the database driver may be loaded.  
  For [SQLite-JDBC](http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC), this might be `<path>/sqlite-jdbc-3.7.2.jar`, depending on the version.  
  For [MySQL](http://dev.mysql.com/downloads/connector/j/), it is `<path>/mysql-connector-java-5.1.18-bin.jar`
* **-d**, **--dbdriver**  
  The class name of the database driver.  
  For SQLite, I use `org.sqlite.JDBC`.  
  For MySQL, this is `com.mysql.jdbc.Driver`.
* **-e**, **--environment**  
  The environment string used in the Arden2ByteCode runtime environment.  JDBC connection strings imply that a JDBC environment is set up.  
  For SQLite, I set this to: `jdbc:sqlite:<dbfile>`. You can also use a blank `<dbfile>` to use a in-memory DB.  
  For MySQL, things are more complicated. Read [Connector/J docs](http://dev.mysql.com/doc/refman/5.1/en/connector-j-reference-configuration-properties.html) for more information.

### Input Files
All further command line arguments will be regarded as input files.

## Examples
* `arden2bytecode -r hello_world.mlm`  
  Will compile and run `hello_world.mlm` without saving to .class file.
* `arden2bytecode -o hello_world.class -c hello_world.mlm`  
  Compile `hello_world.mlm` and save to `hello_world.class`. The MLM will not be run.
* `arden2bytecode -r hello_world.class`  
  Run already compiled MLM `hello_world.class`.
* `arden2bytecode -nr hello_world.mlm`  
  Run MLM without displaying logo.
* `arden2bytecode -nvc *.mlm`  
  Compile all MLMs in one directory verbosely. Don't display logo.  
  This will only work if your command line interpreter 
  supports expansion of `*` arguments.
