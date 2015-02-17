/*
 * Copyright (c) 2015 Maxcode B.V.
 */
Popover = function ($parent, popoverSelector, postURL) {

    var $popover = $(popoverSelector);
    var $alert = $popover.find('.alert');

    $popover.find('.btn-close').on('click', function () {
        toggleVisibility();
    });

    function toggleVisibility()
    {
        var s = ($popover.css('display') == "none") ? 'block' : 'none';
        $popover.css('display', s);

        if (s == 'none') {
            resetAlert();
        }
    }

    $parent.on('click', updateView);

    function updateView () {
        toggleVisibility();

        var pointArr = TT.getElementPosition($parent[0]);
        var top = pointArr[1] + $parent.height();
        var delta = window.innerHeight - top;

        if(delta < $popover.height())
        {
            top = window.innerHeight - $popover.height();
        }

        var left = pointArr[0];
        var fullW = left + $popover.width();
        if(fullW > window.innerWidth)
        {
            left = window.innerWidth - $popover.width();
        }

        $popover.css('left', left);
        $popover.css('top', top);
    }

    function resetAlert() {
        $alert.addClass('hidden');
        $alert.removeClass('alert-danger');
        $alert.removeClass('alert-success');
    }

    $popover.find('.btn-submit').on('click', function () {
        $popover.addClass("disabled");
        resetAlert();

        var $form = $popover.find('form');

        var formData = $form.serializeObject();

        $.ajax({
            url: postURL,
            data: formData,
            method: RequestMethod.POST
        })
            .always(function (data) {
                $alert.removeClass('hidden');

                $popover.removeClass("disabled");

                if(data.error == false || data.error == "false")
                {
                    $alert.html($alert.data('success'));
                    $alert.addClass('alert-success');
                }
                else
                {
                    $alert.html("");
                    var message = '<ul>';

                    for(var i in data.data)
                    {
                        message += '<li>' + data.data[i] + '</li>';
                    }

                    message += '</ul>';

                    $alert.append(message);
                    $alert.addClass('alert-danger');
                }
            });

        return false;
    });
};
