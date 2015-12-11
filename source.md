---
title: Arden2ByteCode Source Code
nav_title: Source Code
nav_order: 6
---
{% include get_url %}

The source code of Arden2ByteCode is made freely available under the terms of the GNU [General Public License](http://www.gnu.org/copyleft/gpl.html).

### View Source Code with Web Browser

The source code of Arden2ByteCode ist hosted at GitHub: [{{ site.compiler.repo }}]({{ site.compiler.repo }})

If you want to contribute, sign up to GitHub and fork the project or, alternatively, ask me [(hflicka@GitHub)](https://github.com/hflicka) to add you to the project.

### Public Git Repository

A public read-only version of the Arden2ByteCode Git repository is located at: [{{ site.compiler.git }}]({{ site.compiler.git }}).

To clone, type:

`git clone {{ site.compiler.git }}`

### Developer Documentation

The documentation for developers is contained in the source code itself and in various README.md [Markdown](https://en.wikipedia.org/wiki/Markdown) files in the according GitHub repositories.

A list of all documentation files is available [here]({{ baseurl }}/docs/developer-documentation).

### History

The programming work was initiated in 2010 by Daniel Grunwald as it can be seen on [GitHub](https://github.com/dgrunwald/arden2bytecode).  
Daniel did the major work, namely the bytecode compiler and runtime environment itself.  
In 2010, I forked the repository and added a command line interface. Still, work has to be done until Arden2ByteCode is production ready.

### Auxiliary Repositories

Other repositories associated with Arden2ByteCode include:

*   [The Eclipse plugin repository]({{ site.plugin.repo }})  
    Clone with: `git clone {{ site.plugin.git }}`
*   [A repository containing the Eclipse update site with binaries for the Arden Syntax Editor feature]({{ site.plugin.update_site.repo }})  
    Clone with: `git clone {{ site.plugin.update_site.git }}`
*   [The repository of this website you're looking at]({{ site.homepage.repo }})  
    Clone with: `git clone {{ site.homepage.git }}`
