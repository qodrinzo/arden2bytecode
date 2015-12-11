---
title: Bug reports
nav_title: Bug reports
nav_order: 6
custom_js:
 - ../js/showdown.min.js
---

If you find a bug in Arden2ByteCode, please post it at [{{ site.compiler.repo }}/issues/new]({{ site.compiler.repo }}/issues/new).

### Open issues of [Arden2ByteCode]({{ site.compiler.repo }}) at GitHub

<ul id="arden2bytecode" class="issues"><li style="list-style-type: none;">Loading (requires JavaScript)...</li></ul>

* * *

### Open issues of the [Arden Syntax Eclipse plugin]({{ site.plugin.repo }}) at GitHub

<ul id="eclipseplugin" class="issues"><li style="list-style-type: none;">Loading (requires JavaScript)...</li></ul>


<script type="text/javascript">
var converter = new showdown.Converter();

function escapeHtml(str) {   
	return (str + '')
		.replace(/&/g,'&amp;')
		.replace(/</g,'&lt;')
		.replace(/>/g,'&gt;');
}

function formatIssue(issue) {
	return '<li class="issue"><span class="issuetitle">'
				+ '<a href="' + issue.html_url
				+ '">' + escapeHtml(issue.title)
				+ '</a>'
				+ '<span class="issueowner"> - posted by: <a href="' + issue.user.html_url + '">'
				+ issue.user.login + '</a></span></span>'
				+ '<div class="issuebody">' + converter.makeHtml(issue.body) + '</div></li>';
}

function queryIssues(issuesurl, divselector, clear) {
	clear = typeof clear !== 'undefined' ? clear : true;
    $.ajax({
		url: issuesurl,
		dataType: 'jsonp'
	}).done(function(result) {
		if (clear) {
			$(divselector).empty();
		}
		$.each(result.data, function(index, issue) {
			$(divselector).append(formatIssue(issue));
		});
		if (result.meta.Link) { // paging
			var next = result.meta.Link.filter(function(obj){return obj[1]['rel']==="next";});
			if (next.length) {
				queryIssues(next[0][0].replace(/callback=\w*&/, ''), divselector, false);
			}
		}
	});
}

$(function() {
	queryIssues('{{ site.compiler.api }}/issues', '#arden2bytecode');
	queryIssues('{{ site.plugin.api }}/issues', '#eclipseplugin');
});
</script>
