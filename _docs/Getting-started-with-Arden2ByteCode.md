---
# GENERATED - DO NOT EDIT
title: Getting started with Arden2ByteCode
---
In the following, I will explain how to run a simple Hello World program with Arden2ByteCode.

Arden2ByteCode is a Java program, thus it requires a Java Runtime Environment (JRE) on your machine. If you already have such a JRE, you can proceed with step 2.

## Step 1: Install a Java Runtime Environment (JRE)

1. Point your browser to the Java Download site: [http://java.com/download](http://java.com/download)

1. Download the installation package and follow the setup instructions.

1. Java should be installed now.

## Step 2: Download and start the Arden2ByteCode compiler

1. Click the 'Download'-button on the Arden2ByteCode project page.

1. Choose the most recent binary package either as .zip-file for Windows or as .tar.gz-file for Linux and MacOS. Download to a directory of your choice.

1. Extract the downloaded binary package to a directory of your choice.

1. Start a command line interpreter (Windows) or terminal window (MacOS, Linux).

1. Change to the directory you extracted the files to:  
   `cd /path/to/arden2bytecode`

1. Type  
   `arden2bytecode -?` (Windows) or  
   `./arden2bytecode -?` (Linux, MacOS)  
   to display the options of the arden2bytecode compiler.

1. Type  
   `arden2bytecode -r hello_world.mlm` (Windows) or  
   `./arden2bytecode -r hello_world.mlm` (Linux, MacOS)  
   to run the Hello World program supplied with the binary package.

1. You should see the copyright note displayed with every start of arden2bytecode and the words 'Hello World!' as output of the command entered before.
