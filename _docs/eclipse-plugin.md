---
# GENERATED - DO NOT EDIT
title: Eclipse Plugin
category: Getting Started
nav_order: 8
---
{% include get_url.liquid %}
If you want to express your medical knowledge with [Arden Syntax](https://en.wikipedia.org/wiki/Arden_syntax) you can use Arden4Eclipse, an Arden Syntax Editor for the [Eclipse IDE](https://eclipse.org/). It integrates Arden2ByteCode so Arden Syntax code can be easily written as well as executed.

Screenshots can be found at the [homepage](https://plri.github.io/arden2bytecode/arden4eclipse/).

The repository is available at <https://github.com/PLRI/ardensyntax-eclipse-plugin>.

## Features
The following convenient features are provided after installing Arden4Eclipse:

+ Syntax coloring
+ Error checking
+ Code completion and templates
+ Rename refactoring
+ Code formatting
+ Outline view and code folding

## Installation
If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at <https://www.eclipse.org/downloads/>.  

You can choose between two ways of installing the Arden4Eclipse:

+ via Eclipse Marketplace (**recommended**)
    0. Drag and drop the following Button onto a running Eclipse window to install the Arden4Eclipse: [![Install Button](http://marketplace.eclipse.org/sites/all/themes/solstice/public/images/components/drag-drop/installbutton.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag to your running Eclipse workspace to install Arden4Eclipse")
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.
+ via Eclipse Update Site (more complicated, but faster)
    0. Go to _Help_ &rArr; _Install new Software..._ in the Eclipse menu.
    0. Enter `https://plri.github.io/ardensyntax-eclipse-plugin/update/` as a Software Site into the topmost field. Note that loading the sites content may take a while ("*Pending&hellip;*" entry in the list).
    0. Mark the _Arden Syntax Editor_ and _Arden2ByteCode Integration_ (optional) to be installed. Click next.
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.

## Using the Editor
Once Arden4Eclipse is installed, hit `ctrl+n` and create a new _Arden Syntax Project_ or add a new _MLM_ file to an existing project (_Java Project_, _General Project_, etc.). When Eclipse prompts if you want to add the Xtext Nature to the project, choose "yes" to activate the Arden Syntax Editor.

Try inserting a for-loop: Place the cursor into the `logic` slot, hit `ctrl+space` to bring up the content assist window and select _for - iterate over sequence_.  
To rename a variable, select it and hit `shift+alt+r`. You can format (indent) the MLM by hitting `ctrl+shift+f`.  

## Running MLMs
To run an MLM with Arden2ByteCode, right-clicking on it and selecting _Run As&hellip;_ &rArr; _Medical Logic Module_.

When starting a MLM, the output should be displayed in the console window. You may also be prompted for input in that window if you programmed your MLM that way.

Further, you can edit the command line options being passed to Arden2ByteCode by creating and editing a launch configuration in the _Run Configurations_ dialog. This dialog can be opened by right-clicking the MLM and selecting _Run As&hellip;_ &rArr; _Run Configurations&hellip;_.
