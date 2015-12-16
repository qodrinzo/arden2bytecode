$(function() {

    var $template_button = $('<button type="button" class="btn btn-default btn-xs clipboard-btn" />');
    var $clip_image = $('<img src="../images/clippy.svg" width="13" alt="Copy to clipboard">');
    $clip_image.appendTo($template_button);

    $('.clipboard-target').each(function(index) {
        var target_id = 'clipboard-target-' + index;
        $(this).attr('id', target_id);

        var $button = $template_button.clone();
        $button.attr({
            'data-clipboard-target' : '#'+target_id
        });

        $button.insertAfter($(this));
    });

    // default tooltip
    $('.clipboard-btn').tooltip({title: 'Copy to clipboard', placement: 'bottom', trigger: 'hover'});

    // reset tooltip message when leaving button
    $('.clipboard-btn').mouseleave(function() {
        $(this).attr('title', 'Copy to clipboard').tooltip('fixTitle').tooltip('hide');
    });

    // init clipboard
    var clipboard = new Clipboard('.clipboard-btn');

    clipboard.on('success', function(e) {
        e.clearSelection();
        $(e.trigger).attr('title', 'Copied!').tooltip('fixTitle').tooltip('show');
    });
    clipboard.on('error', function(e) {
        $(e.trigger).attr('title', 'Not supported :(').tooltip('fixTitle').tooltip('show');
    });
});
