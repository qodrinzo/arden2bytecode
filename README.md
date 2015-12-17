Website for the Arden2ByteCode project
======================================
Accessible here: <https://plri.github.io/arden2bytecode>

Overview
--------
This branch (gh-pages) contains the website for Arden2ByteCode. It is hosted via [Github Pages](https://pages.github.com/).

The website is made with [Jekyll](https://github.com/jekyll/jekyll) and provides basic information about the project, associated downloads, documentation and source code repositories as well as responsible people.

The branch contains a [script](makedocs) to include the [documentation wiki files](https://github.com/PLRI/arden2bytecode/wiki) of Arden2ByteCode.

Usage
---------------
See [Using Jekyll with Pages](https://help.github.com/articles/using-jekyll-with-pages/#keeping-jekyll-up-to-date) and the [Jyekyll Documentation](https://jekyllrb.com/docs/home/).

To clone the gh-pages branch use:

    git clone -b gh-pages https://github.com/PLRI/arden2bytecode.git

Updating the wiki file pages (requires Python):

    rm -rf _docs/
    ./makedocs    

Running a local server:

0. Install [Ruby](https://www.ruby-lang.org/en/downloads/) (version > 2.0.0)
0. Install Bundler: `gem install bundler`
0. Go to the project folder
0. Install github-pages gem: `bundle install`
0. Start local server: `bundle exec jekyll serve --baseurl '/arden2bytecode'`
0. Go to <http://127.0.0.1:4000/arden2bytecode/>
