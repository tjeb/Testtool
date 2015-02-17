/*
 * Copyright (c) 2015 Maxcode B.V.
 */

function AttachmentUploader(attachedFileTemplate_)
{
    var cachedAttachmentTr = {};
    var preloader = $('.preloader-container');
    var table = $('#documentTypesTable');
    var fileSelect = $('#fileSelect');
    var fileNameInput = $('input#name');
    var currentTypeId;
    var form = $('#uploadForm');
    var fileToUpload;
    var fileAddedCallback;

    this.triggerFileUpload = function(currentTypeId_, fileAddedCallback_)
    {
        fileAddedCallback = fileAddedCallback_;
        currentTypeId = currentTypeId_;
        fileSelect.click();
    };

    fileSelect.change(function(event){
        var fileName = event.currentTarget.value;
        var separator = "/";
        if(fileName.indexOf("\\") > 0)
        {
            separator = "\\"
        }

        var arr = fileName.split(separator);
        if(arr.length > 0)
        {
            fileName = arr[arr.length-1];
        }

        fileToUpload = event.target.files[0];
        fileNameInput.val(fileName);
        form.submit();
    });

    form.submit(function(event)
    {
        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        var fileName = form.serializeObject()['name'];
        var fileData = new FormData();
        fileData.append('name', fileName);
        fileData.append('typeId', currentTypeId);
        fileData.append('file', fileToUpload);

        table.addClass('table-disable');
        preloader.removeClass('hidden');

        $.ajax({
            url: TT.urls.attachmentsAPI,
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
            else
            {
                alertify.success("File attached successfully");
                if(!cachedAttachmentTr.hasOwnProperty(currentTypeId))
                {
                    cachedAttachmentTr[currentTypeId] = $('#attachments-' + currentTypeId)
                }

                var latestItem = cachedAttachmentTr[currentTypeId].prepend(TT.templateFill(attachedFileTemplate_, data_.data));
                fileAddedCallback(latestItem);

                reEnableTable();
            }
        }

        function onError()
        {
            alertify.error("An error occurred");
            reEnableTable();
        }

        function reEnableTable()
        {
            table.removeClass('table-disable');
            form[0].reset();
            preloader.addClass('hidden');
        }

        //return false;
    });
}