---
title: Downloads
nav_title: Downloads
nav_order: 3
custom_js:
  - {file: downloads_query.js, foot: true}
---
{% include get_url %}

## Arden2ByteCode

#### Binaries

The most recent binary packages can be found at the GitHub project [downloads listing]({{ site.compiler.repo }}/archives/master)

Here is a mirror of that listing:

<ul class="list-group" id="downloads">
  <li class="list-group-item disabled">Loading (requires JavaScript)&hellip;</li>
</ul>

<script type="text/javascript">
$(function() {
	queryDownloads('{{site.compiler.api}}/releases/latest', '#downloads');
});
</script>

#### Source Packages

The most recent source package is in the master branch of the [Git repository]({{ site.compiler.repo }}).  
Clone with:

    $ git clone {{ site.compiler.git }}

You can download it from GitHub as [.tar.gz]({{ site.compiler.repo }}/tarball/master) or [.zip]({{ site.compiler.repo }}/zipball/master).  
To build, follow the [instructions]({{ baseurl }}/docs/compiling-arden2bytecode-from-source) in the documentation wiki. It's basically typing `ant` at the command prompt.

{% comment %}
#### Changelog

{{ site.docs | where:'title','Changelog' }}
{% endcomment %}

***

## Arden4Eclipse
To install Arden4Eclipse, drag and drop the following button onto a running Eclipse window: [![Install Button](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/components/drag-drop/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=209263 "Drag and drop into a running Eclipse workspace to install Arden Syntax Tools")

Then follow the instructions on [Getting started with Arden4Eclipse]({{ baseurl }}/arden4eclipse/#getting-started).
