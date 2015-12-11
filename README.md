Website for the Arden2ByteCode project
======================================
Accessible here: <https://plri.github.io/arden2bytecode>

Overview
--------
This branch (gh-pages) contains the website for Arden2ByteCode. It is hosted via [Github Pages](https://pages.github.com/).

The website is written in [Jekyll](https://github.com/jekyll/jekyll) and provides basic information about the project, associated downloads, documentation and source code repositories as well as responsible people.

The branch has a [submodule](https://git-scm.com/docs/git-submodule) in order to include the [update site repository](https://github.com/PLRI/ardensyntax-eclipse-plugin-update-site) for the Arden Syntax Eclipse plugin.
It also contains a [script](makedocs) to include the [documentation wiki files](https://github.com/PLRI/arden2bytecode/wiki) of Arden2ByteCode.

Usage
---------------
See [Using Jekyll with Pages](https://help.github.com/articles/using-jekyll-with-pages/#keeping-jekyll-up-to-date) and [Jyekyll Documentation](https://jekyllrb.com/docs/home/).

Don't forget to initialize the submodule while cloning:

    git clone --recursive -b gh-pages https://github.com/PLRI/arden2bytecode.git

To update the submodule to a recent version use:

    git submodule foreach git pull origin master

Updating the wiki file pages:

    rm -rf _docs/
    ./makedocs    

Running a local server:

0. Install [Ruby](https://www.ruby-lang.org/en/downloads/) (version > 2.0.0)
0. Install Bundler: `gem install bundler`
0. Install github-pages gem: `bundle install`
0. Start local server: `bundle exec jekyll serve --baseurl '/arden2bytecode'`
0. Go to <http://127.0.0.1:4000/arden2bytecode/>
