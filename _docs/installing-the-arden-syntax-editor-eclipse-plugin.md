---
# GENERATED - DO NOT EDIT
title: Installing the Arden Syntax Editor Eclipse plugin
---
There is an Eclipse plugin including Arden2ByteCode available that simplifies editing and starting MLM files.

Two general ways of installing that plugin are available:

* via Eclipse Marketplace
* via the Eclipse "Install new Software..." wizard

Both ways require you to have **Eclipse version 3.7**. Version 4 will not work as far as I know.
As of other Eclipse versions, I don't know.

## Installation via Eclise Marketplace

To install via Eclipse Marketplace, drag this link onto a running Eclipse window:  
[Drag 'n drop on Eclipse](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263)

Now you can follow the installation procedure.

## Installation via Eclipse "Install new Software..." wizard

1. Start Eclipse version 3.7 (Indigo)
2. Click on **Help -> Install new Software...** in the menu
3. Uncheck the **Contact all update sites during install to find required software** 
   checkbox to speed up the installation process
4. Add <http://arden2bytecode.sf.net/eclipse/update/> as a software site. The name given does not matter.  
   Now Eclipse loads the update site's contents as well as that of Eclipse. This may take a while as Eclipse servers are slow and there is much useless data transmitted.
5. Mark the **Arden Syntax Editor** to be installed. Click next.
6. Follow the installation procedure. When asked whether to install unsigned content, click OK.
7. When the Editor is installed, Eclipse will ask you if you want to add the xText nature to a project when opening a MLM (*.mlm file).
8. If you added the xText Nature, you should see the syntax of Medical Logic Modules being highlighted.
