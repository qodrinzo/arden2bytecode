---
title: About Arden2ByteCode
nav_title: Arden2ByteCode
nav_order: 1
redirect_from: /about/
permalink: /
---
{% include get_url.liquid %}

The [Arden Syntax](https://en.wikipedia.org/wiki/Arden_syntax) is a standardized language that can be used to express medical knowledge.
Arden2ByteCode is a newly developed open source compiler for the Arden Syntax. Arden2ByteCode runs on Java Virtual Machines (JVM) and translates Arden Syntax directly to Java bytecode (JBC) executable on JVMs. It also serves as runtime environment for execution of the compiled bytecode.

For straightforward use there is an [Arden Syntax Editor plugin]({{ baseurl }}/arden4eclipse) for the Eclipse IDE which integrates Arden2ByteCode so Arden Syntax code can be written and executed.

Arden2ByteCode was initially developed at the [Technische Universität Braunschweig](https://www.tu-braunschweig.de/) in collaboration between the [Peter L. Reichertz Institute for Medical Informatics](https://plri.de/en/forschung/projekte/arden2bytecode) and the [Institute for Programming and Reactive Systems](https://www.ips.cs.tu-bs.de/).

Most programming work was done in 2010 by [Daniel Grunwald](https://github.com/dgrunwald).


## Arden2ByteCode Paper
To cite Arden2ByteCode in publications, use:

Gietzelt M, Goltz U, Grunwald D, Lochau M, Marschollek M, Song B and Wolf K-H.  
**Arden2ByteCode: A one-pass Arden Syntax compiler for service-oriented decision support systems based on the OSGi platform.**  
Comput Methods Programs Biomed. 2012;106(2):114-25.  
PMID: [22206864](https://www.ncbi.nlm.nih.gov/pubmed/22206864)  
DOI: [10.1016/j.cmpb.2011.11.003](https://dx.doi.org/10.1016/j.cmpb.2011.11.003)

A BibTeX file is available [here]({{ baseurl }}/download/arden2bytecode.bib).


## Getting Started
If you want to get started quickly [download]({{ baseurl }}/download) the compiler as a binary package for your platform and read the [Installation]({{ baseurl }}/docs/installation) wiki page.

Do you have problems to get started or ideas on how to improve Arden2ByteCode? Then, [please contact us]({{ baseurl }}/contact)!   
You might wish to submit your issues, ideas or proposals via the [Arden2ByteCode issue tracker]({{ site.compiler.repo }}/issues ).
