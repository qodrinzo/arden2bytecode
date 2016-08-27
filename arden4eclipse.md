---
title: About Arden4Eclipse
nav_title: Arden4Eclipse
nav_order: 2
custom_js:
  - {file: lightbox.min.js}
  - {file: lightbox_options.js}
  - {file: clipboard.min.js}
  - {file: clipboard_button.js}
custom_css:
  - {file: lightbox.css}
---
{% include get_url.liquid %}

If you want to express your medical knowledge with [Arden Syntax](https://en.wikipedia.org/wiki/Arden_syntax) you can use Arden4Eclipse, an Arden Syntax Editor for the [Eclipse IDE](https://eclipse.org/). It integrates Arden2ByteCode so Arden Syntax code can be easily written as well as executed.

Arden4Eclipse is made with the [xText](https://www.eclipse.org/Xtext/) framework.

## Features
The following convenient features are provided after installing Arden4Eclipse:

+ Syntax coloring
+ Syntax error checking
+ Code completion and templates
+ Rename refactoring
+ Code formatting
+ Outline view and code folding

## Screenshots
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

## Getting started
If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at <https://www.eclipse.org/downloads/>.  

You can choose between two ways of installing the Editor:

+ via Eclipse Marketplace (**recommended**)
    0.  Drag and drop the following Button onto a running Eclipse window to install the Arden Syntax Tools: [![Install Button](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/components/drag-drop/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag and drop into a running Eclipse workspace to install Arden Syntax Tools")
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.
+ via Eclipse Update Site (more complicated, but faster)
    0.  Go to *Help* &rArr; *Install new Software...* in the Eclipse menu.
    0.  (*Optional*) Uncheck the *Contact all update sites during install to find required software* checkbox. This is useful to speed up the installation process
    0. Enter <code class="clipboard-target">{{ updateurl }}</code> as a Software Site into the topmost field. Note that loading the Software Sites content may take a while ("*Pending&hellip;*" entry in the list).
    0.  Search and mark the *Arden Syntax Editor* to be installed. Click next.
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.

Once the Arden Syntax Editor is installed, create a new Project (*Java Project*, *General Project*, etc.). When opening or creating a file with the `.mlm` extension in the project, Eclipse will prompt you if you want to add the xText Nature. Choose "yes" to activate the Arden Syntax Editor.
You should now see the syntax of `.mlm` files in that project being highlighted.

To create a new MLM in an empty file hit `ctrl+space` (default) to bring up the content assist window, and select *New module*.  
To run the MLM, you can start Arden2ByteCode by right-clicking on the file and selecting *Run As&hellip;* &rArr; *Medical Logic Module*.

Be sure to have a look at the [documentation]({{ baseurl }}/docs) for more information about writing MLMs in Arden Syntax.

Do you have problems to get started or ideas on how to improve Arden4Eclipse? Then, [please contact us]({{ baseurl }}/contact)!   
You might wish to submit your issues, ideas or proposals via the [Arden4Eclipse issue tracker]({{ site.plugin.repo }}/issues).
