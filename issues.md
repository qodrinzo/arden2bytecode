---
title: Bug reports
nav_title: Bug reports
nav_order: 6
custom_js:
  - {file: showdown.min.js, foot: true}
  - {file: issues_query.js, foot: true}
---

If you find a bug in Arden2ByteCode, please post it at [{{ site.compiler.repo }}/issues/new]({{ site.compiler.repo }}/issues/new).

### Open issues of [Arden2ByteCode]({{ site.compiler.repo }}) at GitHub

<ul id="arden2bytecode" class="issues"><li style="list-style-type: none;">Loading (requires JavaScript)...</li></ul>

* * *

### Open issues of the [Arden Syntax Eclipse plugin]({{ site.plugin.repo }}) at GitHub

<ul id="eclipseplugin" class="issues"><li style="list-style-type: none;">Loading (requires JavaScript)...</li></ul>

<script type="text/javascript">
$(function() {
	queryIssues('{{ site.compiler.api }}/issues', '#arden2bytecode');
	queryIssues('{{ site.plugin.api }}/issues', '#eclipseplugin');
});
</script>
