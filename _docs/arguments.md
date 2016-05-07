---
# GENERATED - DO NOT EDIT
title: Arguments
category: Getting Started
nav_order: 6
---
{% include get_url %}
In Arden2ByteCode, you can pass arguments to Medical Logic Modules (MLMs) and access them in the MLM via the `ARGUMENT` identifier.

To pass arguments, use the **-a** flag of Arden2ByteCode. The string following the **-a** flag must be a valid constant Arden Syntax expression. Otherwise a parsing error will occur.  
To be a valid Arden Syntax expression, a string must be surrounded by quotes which, in turn, must be properly escaped when being passed on the command line.  
The Windows shell and Bash use backslashes to escape quotes.

Windows shell example:

> arden2bytecode -a "(\"arg 1\",\"arg 2\")" "(34.5,\"arg 4\")" -r arguments.mlm

An example of how to pass arguments to a MLM and process them is given here:  
<https://github.com/PLRI/example-mlms/tree/master/passing%20arguments>

Concerning how to use the **-a** flag, you may also look at the [command line options]({{ baseurl }}/docs/command-line-options/) wiki page.
