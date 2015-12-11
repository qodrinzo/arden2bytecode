---
title: Arden2ByteCode Downloads
nav_title: Downloads
nav_order: 3
---
{% include get_url %}

### Binary Packages

The most recent binary packages can be found at the GitHub project [downloads listing]({{ site.compiler.repo }}/archives/master)

Here is a mirror of that listing:

<ul id="downloads"><li style="list-style-type: none;">Loading (requires JavaScript)...</li></ul>
<script type="text/javascript">
$(function() {
    $.ajax({
		url: '{{site.compiler.api}}/downloads',
		dataType: 'jsonp'
	}).done(function(result) {
		$('#downloads').empty();
		$.each(result.data, function(index, value) {
			$('#downloads').append(
				'<li><a href="' + value.html_url
				+ '">' + value.name
				+ '</a> - ' + value.description + '</li>');
		});
	});
});
</script>

<!-- TODO changelog
<?php
	echo render_file('Changelog', '<h2><a href="/docs/changelog">$title</a></h2>');
?>
</div>
-->

### Source Packages

The most recent source package is in the master branch of the [Git repository]({{ site.compiler.repo }}).  
Clone with:

    $ git clone /arden2bytecode.git

You can download it from GitHub as [.tar.gz]({{ site.compiler.repo }}/tarball/master) or [.zip]({{ site.compiler.repo }}/zipball/master).  
To build, follow the [instructions]({{ baseurl }}/docs/compiling-arden2bytecode-from-source) in the documentation wiki. It's basically typing `ant` at the command prompt.
