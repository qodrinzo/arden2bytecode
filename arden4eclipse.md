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

Arden4Eclipse is made with the [Xtext](https://www.eclipse.org/Xtext/) framework.

## Features
The following convenient features are provided after installing Arden4Eclipse:

+ Syntax coloring
+ Error checking
+ Code completion and templates
+ Rename refactoring
+ Code formatting
+ Outline view and code folding

## Screenshots
These screenshots show the plugin while writing and running Medical Logic Modules (MLM):
<div class="row">
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_template.png" class="thumbnail" data-lightbox="screens" data-title="Warnings and templates">
      <img src="{{ baseurl }}/images/plugin_screen_template.png" alt="...">
    </a>
  </div>
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_rename.png" class="thumbnail" data-lightbox="screens" data-title="Renaming variables">
      <img src="{{ baseurl }}/images/plugin_screen_rename.png" alt="...">
    </a>
  </div>
  <div class="col-xs-6 col-md-4">
    <a href="{{ baseurl }}/images/plugin_screen_run.png" class="thumbnail" data-lightbox="screens" data-title="Running MLMs">
      <img src="{{ baseurl }}/images/plugin_screen_run.png" alt="...">
    </a>
  </div>
</div>

## Getting started
If you don't have Eclipse yet, you may download the Eclipse IDE for Java Developers at <https://www.eclipse.org/downloads/>.  

You can choose between two ways of installing the Arden4Eclipse:

+ via Eclipse Marketplace (**recommended**)
    0.  Drag and drop the following Button onto a running Eclipse window to install the Arden4Eclipse: [![Install Button](http://marketplace.eclipse.org/sites/all/themes/solstice/public/images/components/drag-drop/installbutton.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag to your running Eclipse workspace to install Arden4Eclipse")
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.
+ via Eclipse Update Site (more complicated, but faster)
    0.  Go to *Help* &rArr; *Install new Software...* in the Eclipse menu.
    0. Enter <code class="clipboard-target">{{ updateurl }}</code> as a Software Site into the topmost field. Note that loading the sites content may take a while ("*Pending&hellip;*" entry in the list).
    0. Mark the *Arden Syntax Editor* and *Arden2ByteCode Integration* (optional) to be installed. Click next.
    0. Follow the installation procedure. When asked if you want to install unsigned software, click OK.

Once Arden4Eclipse is installed, hit `ctrl+n` and create a new *Arden Syntax Project* or add a new *MLM* file to an existing project (*Java Project*, *General Project*, etc.). When Eclipse prompts if you want to add the Xtext Nature to the project, choose "yes" to activate the Arden Syntax Editor.

Try inserting a for-loop: Place the cursor into the `logic` slot, hit `ctrl+space` to bring up the content assist window and select *for - iterate over sequence*.  
To rename a variable, select it and hit `shift+alt+r`. You can format (indent) the MLM by hitting `ctrl+shift+f`.  
To run the MLM with Arden2ByteCode, right-clicking on it and selecting *Run As&hellip;* &rArr; *Medical Logic Module*.

Be sure to have a look at the [documentation]({{ baseurl }}/docs) for more information about writing MLMs in Arden Syntax.

Do you have problems to get started or ideas on how to improve Arden4Eclipse? Then, [please contact us]({{ baseurl }}/contact)!   
You might wish to submit your issues, ideas or proposals via the [Arden4Eclipse issue tracker]({{ site.plugin.repo }}/issues).
