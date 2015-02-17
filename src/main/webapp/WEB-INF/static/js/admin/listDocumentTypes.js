/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var table = $('#documentTypesTable');
    var trTemplate = TT.getTemplate(".template-element");
    var attachedFileTemplate = TT.getTemplate('.template-attached-file');

    $.get(TT.urls.documentTypeAPI, onTypesLoaded);

    function onTypesLoaded(response_) {
        var html = "";
        var items = response_.data;
        var nr = items.length;
        for (var i=0; i < nr; i++) {
            var itemData = items[i];
            var nrJ = itemData.attachedFiles.length;
            var filesHtml = '';
            for (var j = 0; j < nrJ; j++)
            {
                var file = itemData.attachedFiles[j];
                filesHtml += TT.templateFill(attachedFileTemplate, file);
            }
            var str = TT.templateFill(trTemplate, itemData);
            html += str.replace('$[template-attached-file]', filesHtml);
        }

        table.append(html);
        var attachmentUploader = new AttachmentUploader(attachedFileTemplate);

        var trList = table.find('tr.interactive-edit');

        trList.each(function () {
            var trParent = $(this);
            var selects = trParent.find('select');
            var inputs = trParent.find('input, textarea').add(selects);

            var typeId = trParent.data('id');
            var tdAttachments = trParent.find('#attachments-' + typeId);
            var prevMarkedBtn = tdAttachments.find(".marked");
            tdAttachments.find('.mark-attachment').on('click', markAttachment);
            function markAttachment(){
                var self = $(this);
                enableButtons(buttons, false);
                $.ajax({
                    url: TT.urls.attachmentsAPI_mark + "?id=" + self.data('id'),
                    type: RequestMethod.PUT,
                    dataType: ResponseType.JSON,

                    success: function (data_) {
                        enableButtons(buttons, true);
                        if(data_.error)
                        {
                            onError(data_)
                        }
                        else
                        {
                            prevMarkedBtn.removeClass('marked');
                            prevMarkedBtn = $('#' + self.data('parent-id'));
                            prevMarkedBtn.addClass('marked');
                            alertify.success("Attachment marked successfully");
                        }
                    },

                    error: onError
                });

                function onError(data_) {
                    enableButtons(buttons, true);
                    alertify.error("Error, the file cannot be marked as root file, is this an XSD file ?");
                }

                return false;
            }

            tdAttachments.find('.delete-attachment').on('click', deleteAttachment);
            function deleteAttachment(){
                var self = $(this);
                enableButtons(buttons, false);
                $.ajax({
                    url: TT.urls.attachmentsAPI + "?id=" + self.data('id'),
                    type: RequestMethod.DELETE,
                    dataType: ResponseType.JSON,

                    success: function (data_) {
                        enableButtons(buttons, true);
                        if(data_.error)
                        {
                            onError(data_)
                        }
                        else
                        {
                            $('#' + self.data('parent-id')).remove();
                            alertify.success("Attachment removed successfully");
                        }
                    },

                    error: onError
                });

                function onError(data_) {
                    enableButtons(buttons, true);
                    alertify.error("Error, while removing file type");
                }

                return false;
            }

            var tdActionButtons = trParent.find('.action-buttons');
            tdActionButtons.find('.add-file-btn').on('click', function()
            {
                attachmentUploader.triggerFileUpload($(this).data('type-id'), function(newElement_)
                {
                    newElement_.find('.delete-attachment').on('click', deleteAttachment);
                    newElement_.find('.mark-attachment').on('click', markAttachment);
                });

            });

            var buttons = new ActionButtons(
                tdActionButtons.find('.save-btn'),
                tdActionButtons.find('.revert-btn'),
                tdActionButtons.find('.del-btn')
            );

            buttons.saveBtn.on('click', function()
            {
                enableButtons(buttons, false);

                var data = inputsToString(inputs);
                data += "id=" + typeId;

                data.identifier = encodeURIComponent(data.identifier);

                $.ajax({
                    url: TT.urls.documentTypeAPI + "?" + data,
                    type: RequestMethod.PUT,
                    dataType: ResponseType.JSON,
                    success: onSuccess,
                    error: onError
                });

                function onSuccess(data_) {
                    enableButtons(buttons, true);
                    if(data_.error)
                    {
                        onError(data_);
                    }
                    else
                    {
                        resetActionButtonsUI(tdActionButtons, buttons);

                        var nr = inputs.length;
                        for (var i = 0; i < nr; i++)
                        {
                            var input = $(inputs[i]);
                            input.data('bak-value', input.val());
                            input.attr('title', input.val());
                        }

                        alertify.success("File type info updated successfully");
                    }
                }

                function onError(data_)
                {
                    enableButtons(buttons, true);
                    alertify.error("Error while updating --> file type");
                }
            });

            buttons.revertBtn.on('click', function()
            {
                resetActionButtonsUI(tdActionButtons, buttons);

                var nr = inputs.length;
                for (var i = 0; i < nr; i++)
                {
                    var input = $(inputs[i]);
                    input.val(input.data('bak-value').toString());
                }
            });

            buttons.delBtn.on('click', function(){
                enableButtons(buttons, false);
                $.ajax({
                    url: TT.urls.documentTypeAPI + "?id=" + typeId,
                    type: RequestMethod.DELETE,
                    dataType: ResponseType.JSON,

                    success: function (data_) {
                        enableButtons(buttons, true);
                        if(data_.error)
                        {
                            onError(data_)
                        }
                        else
                        {
                            trParent.remove();
                            alertify.success("File type removed successfully");
                        }
                    },

                    error: onError
                });

                function onError(data_) {
                    enableButtons(buttons, true);
                    alertify.error("Error, while removing file type");
                }
            });

            inputs.bind('input', onInputChanged);
            selects.on('change', onInputChanged);

            function onInputChanged() {
                buttons.saveBtn.removeClass("disabled");
                buttons.revertBtn.removeClass("disabled");
                tdActionButtons.removeClass('passive')
            }

            selects.each(function(){
                var self = $(this);
                self.val(self.data('value').toString());
            });
        });
    }

    function resetActionButtonsUI(tdActionButtons_, actionButtons_)
    {
        tdActionButtons_.addClass('passive');
        actionButtons_.saveBtn.addClass('disabled');
        actionButtons_.revertBtn.addClass('disabled');
    }

    function inputsToString(inputs_)
    {
        var result = "";
        var input;
        var nr = inputs_.length;
        for (var i = 0; i < nr; i++)
        {
            input = inputs_[i];
            result += input.name + "=" + encodeURIComponent(input.value) + "&";
        }

        return result
    }

    function enableButtons(actionButtons_, bool_)
    {
        if(bool_)
        {
            actionButtons_.saveBtn.removeClass('disabled');
            actionButtons_.revertBtn.removeClass('disabled');
            actionButtons_.delBtn.removeClass('disabled');
        }
        else
        {
            actionButtons_.saveBtn.addClass('disabled');
            actionButtons_.revertBtn.addClass('disabled');
            actionButtons_.delBtn.addClass('disabled');
        }
    }

    function ActionButtons(saveBtn, revertBtn, delBtn) {
        this.saveBtn = saveBtn;
        this.revertBtn = revertBtn;
        this.delBtn = delBtn;
    }
});