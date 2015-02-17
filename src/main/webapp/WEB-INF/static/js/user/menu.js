/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var $popover = $('#user-settings-popover');
    var $settingsAlert = $popover.find('#settings-alert');
    $popover.find('#settings-btn-cancel').on('click', function () {
        $('#settings-button').click();
    });

    var $settingsButton = $('#settings-button');
    $settingsButton.click(function (event) {
        var s = ($popover.css('display') == "none") ? 'block' : 'none';
        $popover.css('display', s);

        if (s == 'none') {
            resetAlert();
        }

        var pointArr = TT.getElementPosition($settingsButton[0]);
        var y = pointArr[1];
        var delta = window.innerHeight - pointArr[1];

        if(delta < $popover.height())
        {
            y = window.innerHeight - $popover.height();
        }

        var left = pointArr[0];
        var fullW = left + $popover.width();
        if(fullW > window.innerWidth)
        {
            left = window.innerWidth - $popover.width() - 35;
        }

        $popover.css('left', left);
    });

    function resetAlert() {
        $settingsAlert.addClass('hidden');
        $settingsAlert.removeClass('alert-danger');
        $settingsAlert.removeClass('alert-success');
    }

    $popover.find('#settings-btn-submit').on('click', function () {
        $popover.addClass("disabled");
        resetAlert();

        var $form = $popover.find('#passwordForm');

        var formData = $form.serializeObject();

        $.ajax({
            url: TT.urls.userAPI_password,
            data: formData,
            method: RequestMethod.POST
        })
            .always(function (data) {
                $settingsAlert.removeClass('hidden');

                $popover.removeClass("disabled");

                if(data.error == false || data.error == "false")
                {
                    $settingsAlert.html($settingsAlert.data('success'));
                    $settingsAlert.addClass('alert-success');
                    $('#fullName').html(formData['name']);
                }
                else
                {
                    $settingsAlert.html("");
                    var message = '<ul>';

                    for(var i in data.data)
                    {
                        message += '<li>' + data.data[i] + '</li>';
                    }

                    message += '</ul>';

                    $settingsAlert.append(message);
                    $settingsAlert.addClass('alert-danger');
                }
            });
    });
});
