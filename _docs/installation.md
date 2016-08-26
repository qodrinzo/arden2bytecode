---
# GENERATED - DO NOT EDIT
title: Installation
category: Getting Started
nav_order: 3
---
{% include get_url.liquid %}
Arden2ByteCode is a Java program, thus it requires a Java Runtime Environment (JRE) on your machine. If you already have such a JRE, you can proceed with [step 2](#step-2-download-the-arden2bytecode-compiler).


## Step 1: Install a Java Runtime Environment (JRE)

1. Point your browser to the Java Download site: [http://java.com/download](http://java.com/download)

1. Download the installation package and follow the setup instructions.

1. Java should be installed now.


## Step 2: Download the Arden2ByteCode compiler

1. Go to the [releases page](https://github.com/PLRI/arden2bytecode/releases). 

1. Download the most recent binary package either as .zip-file for Windows or as .tar.gz-file for Linux and MacOS.

1. Extract the downloaded binary package to a directory of your choice.


## Step 3: Testing the compiler

1. Start a command line or terminal window:
  - Windows: Type `cmd` in the start menu and hit enter
  - MacOS: Open your Applications folder, then open the Utilities folder and open a terminal
  - Linux: Press `ctrl + alt + t` or find it in the menu

1. Change to the directory you extracted the files to:  
   `cd /path/to/arden2bytecode`

1. Type `arden2bytecode -?` (Windows) or `./arden2bytecode -?` (Linux, MacOS) to display the options of the Arden2ByteCode compiler.

1. If you see a copyright note displayed and a list of options, then Arden2ByteCode is good to go :+1:. You can now try running the [Hello World!]({{ baseurl }}/docs/hello-world!/) example.
