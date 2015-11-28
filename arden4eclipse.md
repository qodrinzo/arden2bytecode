---
layout: page
title: Arden4Eclipse plugin
nav_title: Arden4Eclipse
permalink: /arden4eclipse/
---

There is an Arden Syntax Editor made with [xText](http://www.xtext.org/) available as Eclipse plugin.

### Installation Instructions

You can choose between two ways of installing:

*   via Eclipse Marketplace (recommended, but slow)
*   via Eclipse Update Site (more complicated, but faster)

#### via Eclipse Marketplace

To install via Eclipse Marketplace, follow these steps:

1.  Drag and drop the following Button onto a running Eclipse window to install the Arden Syntax Tools: [![Install Button](http://marketplace.eclipse.org/misc/installbutton.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag and drop into a running Eclipse Indigo workspace to install Arden Syntax Tools") 
2.  Follow the installation procedure. When asked if you want to install unsigned software, click OK.

#### via Eclipse Update Site

To install via the [Arden2ByteCode Update Site]({{ site.plugin.update_site.url }}), follow these steps:

1.  Start a recent version of the Eclipse IDE.  
    If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at [http://www.eclipse.org/downloads/](http://www.eclipse.org/downloads/).
2.  Go to **Help -> Install new Software...** in the Eclipse menu.
3.  Uncheck the **Contact all update sites during install to find required software** checkbox (this is useful to speed up the installation process).
4.  Add [{{ site.plugin.update_site.url }}]({{ site.plugin.update_site.url }}) as a Software Site.  
    The name you enter for that site does not matter.  
    Note that the loading of the Software Site content may take a while as the Eclipse servers are slow. During this time, you will see a "Pending..." notice.
5.  Uncheck the **Group items by category** checkbox.
6.  Mark the "Arden Syntax Editor" to be installed. Click next.
7.  Follow the installation procedure. When asked if you want to install unsigned software, click OK.
8.  Once the Arden Syntax Editor is installed, Eclipse will prompt you if you want to add the xText Nature to your project when opening or creating an MLM (*.mlm) file.  
    Choose "yes" to activate the Arden Syntax Editor.
9.  You should now see the Syntax of `.mlm` files in that project being highlighted.