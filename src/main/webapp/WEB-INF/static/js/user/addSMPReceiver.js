/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function()
{
    var fileToUpload;
    var $form = $('#addAPForm');
    var $buttons = $form.find('.action-buttons');
    var $fileSelect = $form.find('#fileSelect');

    var alerts$ = {};

    $('.input-warning').each(function(){
        var self = $(this);
        self.hide();
        alerts$[self.data('parent')] = self;
    });

    $fileSelect.change(function(event){
        fileToUpload = event.target.files[0];
    });

    $form.submit(function()
    {
        $buttons.addClass('disabled');

        var data = $form.serializeObject();

        $.ajax({
            data: data,
            url: TT.urls.userSMPAPI,
            cache: false,
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
                alertify.success("SMP Receiver added successfully");
                //$form[0].reset();

                for (var field in alerts$)
                {
                    alerts$[field].hide();
                }

                resetButtons();
            }
        }

        function onError(event_)
        {
            alertify.alert("An error occurred");

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
            $buttons.removeClass('disabled');
        }

        return false;
    })
});