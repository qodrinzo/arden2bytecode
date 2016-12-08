---
# GENERATED - DO NOT EDIT
title: Hello World!
category: Getting Started
nav_order: 4
---
{% include get_url.liquid %}
The following shows how to output a "Hello World!" message on standard output using Arden Syntax.  

Have a look at this Medical Logic Module (MLM) and save it to a file called `hello_world.mlm`:  

```arden
/*
 * The maintenance category covers general information used for developing
 * this Medical Logic Module (MLM).
 */
maintenance:
    title: Hello World;;
    mlmname: hello_world;;
    arden: Version 2.5;;
    version: 1.81;;
    institution: arden2bytecode authors;;
    author: Hannes Flicka;;
    specialist: Mike Klimek;;
    date: 2016-12-07;;
    validation: testing;;

/*
 * This category contains additional metadata about the medical knowledge
 * contained in this module.
 */
library:
    purpose: Demonstration of Arden Syntax;;
    explanation: Prints 'Hello World!';;
    keywords: hello world; example; Arden Syntax;;
    citations: ;;
    links: 'https://plri.github.io/arden2bytecode/docs/';;

// This section contains - among other stuff - the actual program logic.
knowledge:
    type: data_driven;;

    data:
        // Define the greeting variable to be the string "Hello world!"
        LET greeting BE "Hello, World!";
    ;;

    evoke:
        // This MLM is called directly, so no evoke statement is required.
    ;;

    logic:
        // The 'CONCLUDE' statement decides whether to execute the action slot.
        CONCLUDE TRUE;
    ;;

    action:
        // The actual output.
        WRITE greeting;
    ;;

end:
```

## Running the Hello World example

To run the above MLM, put it in a directory where you can start the Arden2ByteCode compiler and type `arden2bytecode -r hello_world.mlm` (Windows) or `./arden2bytecode -r hello_world.mlm` (Linux, MacOS) on a command prompt.  
The output should be:

    Arden2ByteCode Compiler and Runtime Environment
    Copyright 2010-2016 Daniel Grunwald, Hannes Flicka, Mike Klimek

    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License.

    Message: Hello, World!
    There was no return value.

Now that you have a starting point for working with Arden Syntax, you can learn the Arden Syntax [basics]({{ baseurl }}/docs/basics/) or try out different [command line options]({{ baseurl }}/docs/command-line-options/).
