---
title: Documentation
nav_title: Documentation
redirect_from: "/foo/"
custom_js:
 - https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js
---
{% assign c = site.collections | where:"label","wiki_files" %}
{% if c != empty %}
  {% assign wiki_files = c[0] %}
    {% for file in wiki_files.files %}
{% capture docu_file %}{% include_relative {{file.path }} %}{% endcapture %}
{{ docu_file | markdownify }}
    {% endfor %}
{% endif %}

<h1>TODO</h1> {% comment %}{{ site.github | jsonify }} {% endcomment %}
