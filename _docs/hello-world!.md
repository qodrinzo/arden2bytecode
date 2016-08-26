---
# GENERATED - DO NOT EDIT
title: Hello World!
category: Getting Started
nav_order: 4
---
{% include get_url.liquid %}
The following shows how to output a Hello World! message on standard output using Arden Syntax.  
File `hello_world.mlm`:

	/* This is a comment. */
	maintenance: /* This section covers general information about this Medical Logic Module (MLM) */
	  title:  hello world;;
	  mlmname:  hello_world;;
	  arden:  Version 2.1;;  version:  1.70;;
	  institution:  arden2bytecode authors;;
	  author:  Hannes Flicka;;
	  specialist:  ;;
	  date:  2011-09-08;;
	  validation:  testing;;

	library: /* This section contains additional metadata such that you can find this MLM in larger databases */
	  purpose:  demonstration of arden syntax;;
	  explanation:  ;;
	  keywords:  hello world;;
	  citations:  ;;

	knowledge: /* This section contains - among other stuff - the actual program logic. */
	  type:  data-driven;;

	  data:
	      stdout_dest := destination
	        {stdout}; /* The 'destination' statement is implementation specific. */
	          /* For further information, have a look at the documentation of 
	             your runtime environment. In our case, this is arden2bytecode. */
	      ;;

	  evoke:
	      null_event;; 
	  
	  logic:
	      conclude true;;

	  action:
	      write "Hello world!"
	        at stdout_dest; /* The actual output. */
	      ;;

	  urgency:  50;;

	end:

## Running the Hello World example

To run the above MLM, put it in a directory where you can start the Arden2ByteCode compiler and type `arden2bytecode -r hello_world.mlm` on a command prompt.  
The output should be:

	Arden2ByteCode Compiler and Runtime Environment
	Copyright 2010-2011 Daniel Grunwald, Hannes Flicka

	This program is free software; you can redistribute it and/or modify it
	under the terms of the GNU General Public License.

	"Hello world!"
	There was no return value.

Now that you have a starting point for working with Arden Syntax, you can learn the Arden Syntax [basics]({{ baseurl }}/docs/basics/) or try out different [command line options]({{ baseurl }}/docs/command-line-options/).
