---
# GENERATED - DO NOT EDIT
title: Eclipse Plugin
category: Getting Started
nav_order: 8
---
{% include get_url.liquid %}
There is an Eclipse plugin including Arden2ByteCode available that simplifies editing and starting MLM files.

If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at <https://www.eclipse.org/downloads/>. 

You can choose between two ways of installing the Editor:

+ via Eclipse Marketplace (**recommended**)
    0.  Drag and drop the following Button onto a running Eclipse window to install the Arden Syntax Tools: [![Install Button](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/components/drag-drop/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag and drop into a running Eclipse workspace to install Arden Syntax Tools")
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.
+ via Eclipse Update Site (more complicated, but faster)
    0.  Go to *Help* &rArr; *Install new Software...* in the Eclipse menu.
    0.  (*Optional*) Uncheck the *Contact all update sites during install to find required software* checkbox. This is useful to speed up the installation process
    0. Enter `https://plri.github.io/ardensyntax-eclipse-plugin/update/` as a Software Site into the topmost field. Note that loading the Software Sites content may take a while ("*Pending&hellip;*" entry in the list).
    0.  Search and mark the *Arden Syntax Editor* to be installed. Click next.
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.

Once the Arden Syntax Editor is installed, create a new Project (*Java Project*, *General Project*, etc.). When opening or creating a file with the `.mlm` extension in the project, Eclipse will prompt you if you want to add the xText Nature. Choose "yes" to activate the Arden Syntax Editor.
You should now see the syntax of `.mlm` files in that project being highlighted.

To create a new MLM in an empty file hit `ctrl+space` (default) to bring up the content assist window, and select *New module*.  
To run the MLM, you can start Arden2ByteCode by right-clicking on the file and selecting *Run As&hellip;* &rArr; *Medical Logic Module*.
