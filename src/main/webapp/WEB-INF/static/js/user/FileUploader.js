/*
 * Copyright (c) 2015 Maxcode B.V.
 */

function FileUploader()
{
    var preloader = $('.preloader-container');
    var warningDetailsAlert = $('#warningDetailsAlert');
    var warningDetailsArea = warningDetailsAlert.find('p');
    var errorDetailsAlert = $('#errorDetailsAlert');
    var errorDetailsArea = errorDetailsAlert.find('p');
    var fileSelect = $('#fileSelect');
    var form = $('#uploadForm');
    var fileToUpload;

    fileSelect.change(function(event){
        fileToUpload = event.target.files[0];
        errorDetailsAlert.addClass('hidden');
        warningDetailsAlert.addClass('hidden');
    });

    form.submit(function(event)
    {
        event.stopPropagation();
        event.preventDefault();

        form.addClass('table-disable');

        var fileData = new FormData();
        var fileName = form.serializeObject()['name'];
        fileData.append('name', fileName);
        fileData.append('typeId', $('#type').find(":selected").data('id'));
        fileData.append('file', fileToUpload);

        preloader.removeClass('hidden');

        $.ajax({
            url: TT.urls.userFileAPI,
            data: fileData,
            cache: false,
            processData: false,
            contentType: false,
            type: RequestMethod.POST,
            dataType: ResponseType.JSON,
            success: onSuccess,
            error: onError
        });

        function onSuccess(data_)
        {
            if(data_.error)
            {
                onError(data_);
            }
            else if(data_.warning)
            {
                onWarning(data_);
            }
            else
            {
                alertify.success("File uploaded and validated successfully");
                reEnableTable();
            }
        }

        function onWarning(data_)
        {
            alertify.success("File uploaded with warning messages");
            warningDetailsAlert.removeClass('hidden');
            warningDetailsArea.html(data_.message);
            reEnableTable();
        }

        function onError(data_)
        {
            alertify.error("An error occurred");
            errorDetailsAlert.removeClass('hidden');
            errorDetailsArea.html(data_.message);
            reEnableTable();
        }

        function reEnableTable()
        {
            form[0].reset();
            preloader.addClass('hidden');
            form.removeClass('table-disable');
        }

        return false;
    });
}