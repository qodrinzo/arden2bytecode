var converter = new showdown.Converter();

function escapeHtml(str) {
	return (str + '')
		.replace(/&/g,'&amp;')
		.replace(/</g,'&lt;')
		.replace(/>/g,'&gt;');
}

function formatIssue(issue) {
	return '<li class="issue list-group-item"><span class="issuetitle">'
				+ '<a href="' + issue.html_url
				+ '">' + escapeHtml(issue.title)
				+ '</a>'
				+ '<span class="issueowner"> - posted by: <a href="' + issue.user.html_url + '">'
				+ issue.user.login + '</a></span></span>'
				+ '<div class="issuebody">' + converter.makeHtml(issue.body) + '</div></li>';
}

function queryIssues(issuesurl, divselector, countselector) {
    $.ajax({
		url: issuesurl,
		dataType: 'jsonp',
	}).done(function(result) {
        $(divselector).empty();
        $(countselector).empty();

        if(result.meta.status === 403) {
            $(divselector).append('<li class="alert alert-danger list-group-item" role="alert">Too many requests</li>');
            return;
        }

        $(countselector).append(result.data.length);
        if(result.data.length !== 0) {
            $.each(result.data, function(index, issue) {
                $(divselector).append(formatIssue(issue));
            });
        } else {
            $(divselector).append('<li class="list-group-item">None</li>');
        }

		if (result.meta.Link) { // paging
			var next = result.meta.Link.filter(function(obj){return obj[1].rel==="next";});
			if (next.length) {
				queryIssues(next[0][0].replace(/callback=\w*&/, ''), divselector, false);
			}
		}
	});
}
