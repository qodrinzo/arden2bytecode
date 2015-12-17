---
title: Bug reports
nav_title: Bug reports
nav_order: 6
custom_js:
  - {file: showdown.min.js, foot: true}
  - {file: issues_query.js, foot: true}
---

If you find a bug, or have an idea on how to improve Arden4Eclipse, please post it at the [Arden2ByteCode issue tracker]({{ site.compiler.repo }}/issues) or the [Arden4Eclipse issue tracker]({{ site.plugin.repo }}/issues) at GitHub.  

<br/>

<div class="panel panel-default">
  <div class="panel-heading">Open issues of the <a href="{{ site.compiler.repo }}/issues">Arden2ByteCode issue tracker</a> <span id="arden2bytecode-count" class="badge"/></div>
  <ul id="arden2bytecode" class="issues list-group"><li class="list-group-item">Loading (requires JavaScript)...</li></ul>
  <div class="panel-footer">
    <a href="{{ site.compiler.repo }}/issues/new" class="btn btn-primary btn-sm" role="button">Post new issue</a>
  </div>
</div>

<div class="panel panel-default">
  <div class="panel-heading">Open issues of the <a href="{{ site.plugin.repo }}/issues">Arden4Eclipse issue tracker</a> <span id="eclipseplugin-count" class="badge"/></div>
  <ul id="eclipseplugin" class="issues list-group"><li class="list-group-item">Loading (requires JavaScript)...</li></ul>
  <div class="panel-footer">
    <a href="{{ site.plugin.repo }}/issues/new" class="btn btn-primary btn-sm" role="button">Post new issue</a>
  </div>
</div>

<script type="text/javascript">
$(function() {
	queryIssues('{{ site.compiler.api }}/issues', '#arden2bytecode', '#arden2bytecode-count');
	queryIssues('{{ site.plugin.api }}/issues', '#eclipseplugin', '#eclipseplugin-count');
});
</script>
