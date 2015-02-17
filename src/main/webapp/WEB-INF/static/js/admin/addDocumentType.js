/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var form = $('#addDocumentType');
    var buttons = form.find('.save-buttons');

    var alerts$ = {};
    var $name = $('#name');
    var $identifier = $('#identifier');
    var $galaxyMetadataProfile = $('#galaxyMetadataProfile');

    $galaxyMetadataProfile.on('change', function()
    {
        var $options = $(this.selectedOptions);
        var value = $options.data('identifier-id');

        if(typeof value == "undefined")
        {
            value = "";
            $identifier.removeAttr('readonly');
        }
        else
        {
            $identifier.attr('readonly', true);
        }

        $name.val($options.html());
        $identifier.val(value);
    });

    $('.alert').each(function(){
        var self = $(this);
        self.hide();
        alerts$[self.data('parent')] = self;
    });

    form.submit(function()
    {
        buttons.addClass('disabled');

        $.ajax({
            url: TT.urls.documentTypeAPI,
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
                alertify.success("File type added successfully");
                $identifier.removeAttr('readonly');
                $galaxyMetadataProfile.find('option:selected').remove();

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
            alertify.error("An error occurred");

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
    })

});