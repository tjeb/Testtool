/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var table = $('#userTable');
    var tmp = $('tr.template-element');
    var trTemplate = tmp.removeClass("template-element").remove()[0].outerHTML;

    $.get(TT.urls.usersAPI, {cache:false}, onUsersLoaded);

    function onUsersLoaded(response_) {
        var html = "";
        var items = response_.data;
        var nr = items.length;
        for (var i=0; i < nr; i++) {
            html += TT.templateFill(trTemplate, items[i]);
        }

        table.append(html);

        var trList = table.find('tr.interactive-edit');

        trList.each(function () {
            var trParent = $(this);
            var selects = trParent.find('select');
            var inputs = trParent.find('input, textarea, email, password').add(selects);

            var id = trParent.data('id');
            var tdActionButtons = trParent.find('.action-buttons');
            var buttons = new ActionButtons(
                tdActionButtons.find('.save-btn'),
                tdActionButtons.find('.revert-btn'),
                tdActionButtons.find('.del-btn')
            );

            buttons.saveBtn.on('click', function()
            {
                enableButtons(buttons, false);

                var data = inputsToString(inputs);
                data += "id=" + id;
                $.ajax({
                    url: TT.urls.usersAPI + "?" + data,
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

                            if(input.attr('type') == "password")
                            {
                                input.val("");
                            }
                        }

                        alertify.success("User info updated successfully");
                    }
                }

                function onError(data_)
                {
                    enableButtons(buttons, true);
                    alertify.error("Error while updating user");
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

                if(!confirm("Do you really want to delete this user and all the user data (access points and owned files)?")) {
                    return;
                }
                enableButtons(buttons, false);
                $.ajax({
                    url: TT.urls.usersAPI + "?userId=" + id,
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
                            alertify.success("User removed successfully");
                        }
                    },

                    error: onError
                });

                function onError(data_) {
                    enableButtons(buttons, true);
                    alertify.error("Error, while removing user");
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