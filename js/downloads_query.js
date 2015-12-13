function queryDownloads(downloadurl, selector) {
    $.ajax({
		url: downloadurl,
		dataType: 'jsonp'
	}).done(function(result) {
		$(selector).empty();
		$.each(result.data, function(index, value) {
			$(selector).append(
				'<li><a href="' + value.html_url
				+ '">' + value.name
				+ '</a> - ' + value.description + '</li>');
		});
	});
}
