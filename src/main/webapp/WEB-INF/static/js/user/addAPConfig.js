/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function()
{
    var fileToUpload;
    var $form = $('#addAPForm');
    var $buttons = $form.find('.action-buttons');
    var $fileSelect = $form.find('#fileSelect');

    var $successAlert = $('#successAlert');
    $successAlert.hide();
    var $successA = $successAlert.find('a');
    var linkTemplate = $successA.attr('href');

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
        $successAlert.hide();
        $buttons.addClass('disabled');

        var data = $form.serializeObject();
        var fileData = new FormData();
        fileData.append('certificate', data.certificate);
        fileData.append('name', data.name);
        fileData.append('url', data.url);
        fileData.append('description', data.description);
        fileData.append('contactEmail', data.contactEmail);
        fileData.append('technicalUrl', data.technicalUrl);

        $.ajax({
            data: fileData,
            url: TT.urls.userAPAPI,
            cache: false,
            processData: false,
            contentType: false,
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
                $successA.attr('href', linkTemplate.replace("#[fileId]", event_.data));
                $successAlert.show();

                alertify.success("File type added successfully");
                $form[0].reset();

                for (var field in alerts$)
                {
                    alerts$[field].hide();
                }

                resetButtons();
                window.location = TT.CONTEXT_PATH + '/html/user/accessPoints/list';
            }
        }

        function onError(event_)
        {
            alertify.error("An error occurred", '', 30);

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