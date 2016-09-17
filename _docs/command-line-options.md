---
# GENERATED - DO NOT EDIT
title: Command Line Options
category: Getting Started
nav_order: 5
---
{% include get_url.liquid %}
### Synopsis
`arden2bytecode [OPTIONS] [FILE]...`

### Options

**Modes (mutually exclusive)**

- **-h**, **-?**, **--help**  
Display help to command line options.
- **-c**, **--compile**  
Compile the input files (must be of `.mlm` type) and write output of compiler to `.class` files. See **-d** for output options.
- **-r**, **--run**  
Run the input files directly. The input files must be either `.mlm` files which will be compiled in-memory or already compiled `.class` MLMs. See **-a** for providing arguments to the MLMs.
- **-e**, **--engine**  
Run an event engine that waits for events or evoke triggers and executes the input MLMs when they are scheduled. It can be terminated via `ctrl+c`. See [Scheduling]({{ baseurl }}/docs/scheduling/) for more information about triggers and events. See **-p** on how to listen to external events.

**Control options**

- **-v**, **--verbose**  
Verbose output mode. For example the input files or received events will be explicitly listed in this mode.
- **-n**, **--nologo**  
Don't display the header with program name and licensing information.
- **-d** _directory_, **--directory** _directory_  
Output directory into which compiled MLM class files are placed, when compiling with **-c**.
- **-a** _argument&hellip;_, **--arguments** _argument&hellip;_  
Specify arguments to the MLM when running with **-r**. Arguments must be valid Arden Syntax constants, separated by spaces, and must be properly escaped by your command line interpreter. See [Arguments]({{ baseurl }}/docs/arguments/) for more information about passing arguments to MLMs.
- **-p** _number_, **--port** _number_  
If specified will start a server that listens for event strings on the given port. Intended to be used when running the event engine with **-e**.  
See [Scheduling]({{ baseurl }}/docs/scheduling/) for more information.

**Execution Context**

- **--cp** _classpath_, **--classpath** _classpath_  
  An additional [classpath](https://en.wikipedia.org/wiki/Classpath_%28Java%29) from where other MLMs may be called. Also allows specifying where the database driver may be loaded from. See [Using Databases]({{ baseurl }}/docs/using-databases/) for more.
- **--db** _classname_, **--dbdriver** _classname_  
  The class name of the database driver. See [Using Databases]({{ baseurl }}/docs/using-databases/) for more.
- **--env** _text_, **--environment** _text_  
  Specify an argument for the execution environment. A JDBC database connection string implies that a JDBC environment will be used. Otherwise a stdio environment handles data input and output via the command line. See [Using Databases]({{ baseurl }}/docs/using-databases/) for more.

### Input Files
All further command line arguments will be regarded as input files.

## Examples

- Compile `module1.mlm` and `module2.mlm` and save the `.class` files to the `modules` directory:

  ```bash
  arden2bytecode -d my_modules -c module1.mlm module2.mlm
  ```

- Run already compiled MLM `hello_world.class`:

  ```bash
  arden2bytecode -r hello_world.class
  ```

- Compile and run `hello_world.mlm` without saving to `.class` file:

  ```bash
  arden2bytecode -r hello_world.mlm
  ```

- Run two MLMs, first `module1.mlm` then `module2.mlm`:

  ```bash
  arden2bytecode -r module1.mlm module2.mlm
  ```

- Run MLM without displaying logo:

  ```bash
  arden2bytecode -nr hello_world.mlm
  ```

- Run the MLM with a list, string, number and duration as arguments:

  ```bash
  arden2bytecode -a '(3.5,2)' '"Jane Doe"' 123 '5 WEEKS'  -r arguments.mlm
  ```

- Compile all MLMs in one directory verbosely. Don't display logo. This will only work if your command line interpreter supports expansion of `*` arguments.

  ```bash
  arden2bytecode -nvc *.mlm
  ```

- Start the event engine for multiple MLMs, and listen verbosely for incoming events on port 9701:

  ```bash
  arden2bytecode -v -p 9701 -e *.mlm
  ```

- Run an MLM which queries an SQLite database:

  ```bash
  arden2bytecode --cp lib/sqlite-jdbc-3.7.2.jar --dbdriver org.sqlite.JDBC --environment jdbc:sqlite:person.sqlite -r sql-example.mlm
  ```

- Start the event engine, listen for events on a port, and use a database to handle MLM queries. Verbosely, no logo.

  ```bash
  arden2bytecode -nv -p 9701 --cp lib/sqlite-jdbc-3.7.2.jar --db org.sqlite.JDBC --env jdbc:sqlite:person.sqlite -e sql-example.mlm
  ```
