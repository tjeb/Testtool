/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function()
{
    var table = $('#listSMPReceivers');
    table.find('tr').each(function()
    {
        var trParent = $(this);
        var actionButtons = trParent.find('.action-buttons');
        actionButtons.find('.remove-btn').on('click', function()
        {
            trParent.addClass('table-disable');
            $.ajax({
                url: TT.urls.userSMPAPI + "?participantId=" + $(this).data('value'),
                type: RequestMethod.DELETE,
                dataType: ResponseType.JSON,

                success: function (data_) {
                    if(data_.error)
                    {
                        onError(data_)
                    }
                    else
                    {
                        trParent.remove();
                        alertify.success("SMP Receiver deleted successfully");
                    }
                },

                error: onError
            });

            function onError(data_) {
                trParent.removeClass('table-disable');
                alertify.alert("Error, while deleting SMP Receiver");
            }
        })
    });
});