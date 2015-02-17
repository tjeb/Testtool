/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var form = $('#addUsers');
    var buttons = form.find('.save-buttons');

    var alerts$ = {};

    $('.alert').each(function(){
        var self = $(this);
        self.hide();
        alerts$[self.data('parent')] = self;
    });

    form.submit(function()
    {
        buttons.addClass('disabled');

        $.ajax({
            url: TT.urls.usersAPI,
            data: form.serializeObject(),
            type: RequestMethod.POST,
            dataType: ResponseType.JSON,
            success: onSuccess,
            error: onError
        });

        function onSuccess(event_)
        {
            if(event_.error)
            {
                onError(event_);
            }
            else
            {
                alertify.success("User added successfully");
                form[0].reset();

                for (var field in alerts$)
                {
                    alerts$[field].hide();
                }

                resetButtons();
            }
        }

        function onError(event_)
        {
            alertify.error("An error occurred: " + event_.message);

            for (var field in alerts$)
            {
                if(typeof event_.data[field] != "undefined")
                {
                    alerts$[field].html(event_.data[field]);
                    alerts$[field].show()
                }
                else
                {
                    alerts$[field].hide();
                }
            }

            resetButtons();
        }

        function resetButtons()
        {
            buttons.removeClass('disabled');
        }

        return false;
    });

});