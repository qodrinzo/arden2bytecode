function queryDownloads(downloadurl, selector) {
    $.ajax({
		url: downloadurl,
		dataType: 'jsonp'
	}).done(function(result) {
		$(selector).empty();

		if(result.meta.status === 403) {
			$(selector).append('<li class="list-group-item list-group-item-danger">Too many requests</li>');
			return;
		}

		if(result.data && result.data.assets && result.data.assets.length !== 0) {
			$.each(result.data.assets, function(index, asset) {
				$(selector).append(
					'<li class="list-group-item">' +
					'<a href="' + asset.browser_download_url + '">' +
					result.data.name + ' (' + asset.name + ')' +
					'</a>' +
					'</li>'

				);
			});
		} else {
			$(selector).append('<li class="list-group-item disabled">No releases found</li>');
		}

	});
}
