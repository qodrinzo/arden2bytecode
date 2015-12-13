---
title: Arden4Eclipse plugin
nav_title: Arden4Eclipse
nav_order: 2
custom_js:
  - {file: lightbox.min.js, foot: true}
  - {file: lightbox_options.js, foot: true}
custom_css:
  - {file: lightbox.css}
---
{% include get_url %}

Arden4Eclipse is an Arden Syntax Editor made with [xText](https://www.eclipse.org/Xtext/) to be used with the Eclipse IDE.

### Features
The following convenient features are provided after installing the plugin:

+ Syntax coloring
+ Syntax error checking
+ Code completion and templates
+ Rename refactoring
+ Code formatting
+ Outline view and code folding

### Screenshots
These screenshots show the plugin while writing and running Medical Logic Modules (MLM):
<div class="row">
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_error.png" class="thumbnail" data-lightbox="screens" data-title="Errors and creating modules">
      <img src="{{ baseurl }}/images/plugin_screen_error.png" alt="...">
    </a>
  </div>
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_rename.png" class="thumbnail" data-lightbox="screens" data-title="Renaming variables">
      <img src="{{ baseurl }}/images/plugin_screen_rename.png" alt="...">
    </a>
  </div>
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_run.png" class="thumbnail" data-lightbox="screens" data-title="Code folding and running an MLM">
      <img src="{{ baseurl }}/images/plugin_screen_run.png" alt="...">
    </a>
  </div>
</div>

### Installation Instructions
You can choose between two ways of installing:

*   via Eclipse Marketplace (recommended, but slow)
*   via Eclipse Update Site (more complicated, but faster)

#### via Eclipse Marketplace
To install via Eclipse Marketplace, follow these steps:

1.  Drag and drop the following Button onto a running Eclipse window to install the Arden Syntax Tools: [![Install Button](https://marketplace.eclipse.org/misc/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag and drop into a running Eclipse Indigo workspace to install Arden Syntax Tools")
2.  Follow the installation procedure. When asked if you want to install unsigned software, click OK.
3.  Once the Arden Syntax Editor is installed, Eclipse will prompt you if you want to add the xText Nature to your project when opening or creating an MLM (*.mlm) file.  
    Choose "yes" to activate the Arden Syntax Editor.
4.  You should now see the Syntax of `.mlm` files in that project being highlighted.

#### via Eclipse Update Site

To install via the [Arden2ByteCode Update Site]({{ updateurl }}), follow these steps:

1.  Start a recent version of the Eclipse IDE.  
    If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at <https://www.eclipse.org/downloads/>.
2.  Go to **Help -> Install new Software...** in the Eclipse menu.
3.  Uncheck the **Contact all update sites during install to find required software** checkbox (this is useful to speed up the installation process).
4.  Add [{{ updateurl }}]({{ updateurl }}) as a Software Site.  
    The name you enter for that site does not matter.  
    Note that the loading of the Software Site content may take a while as the Eclipse servers are slow. During this time, you will see a "Pending..." notice.
5.  Uncheck the **Group items by category** checkbox.
6.  Mark the "Arden Syntax Editor" to be installed. Click next.
7.  Follow the installation procedure. When asked if you want to install unsigned software, click OK.
8.  Once the Arden Syntax Editor is installed, Eclipse will prompt you if you want to add the xText Nature to your project when opening or creating an MLM (*.mlm) file.  
    Choose "yes" to activate the Arden Syntax Editor.
9.  You should now see the Syntax of `.mlm` files in that project being highlighted.
