/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function()
{
    var fileSelect = $('#fileSelect');
    var fileNameInput = $('input#name');
    FileUploader();

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

        fileNameInput.val(fileName);
    });

    fileNameInput.on('click', function(){
        fileSelect.click();
    });
});