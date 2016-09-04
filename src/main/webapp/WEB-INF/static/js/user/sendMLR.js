/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    var $preloaderContainer = $('.preloader-container');
    var $preloaderMessage = $preloaderContainer.find('.well-preloader');
    var $preloaderDialog = $preloaderContainer.find('.modal-dialog');
    var $preloaderUrlArea = $preloaderContainer.find('#urlArea');
    var $preloaderCloseBtn = $preloaderContainer.find('.close');

    $preloaderCloseBtn.on('click', function()
    {
        $preloaderContainer.addClass('hidden');
    });


    var currentFileToSend;
    var currentAP_id;
    var revertUICallback;
    var updateUICallback;

    $('.action-buttons').each(function () {
        var $actionButtons = $(this);
        var $pendingDiv = $actionButtons.find('.pending');
        var $activeDiv = $actionButtons.find('.active');
        var secondsArea = $activeDiv.find('.seconds-area');
        var updateIdx;

        $actionButtons.find('.btn-send-mlr').on('click', function () {
            var self = $(this);

            currentFileToSend = self.data('file-id');

            revertUICallback = function ()
            {
                $pendingDiv.removeClass('hidden');
                $activeDiv.addClass('hidden');
                clearInterval(updateIdx);
                secondsArea.html(0)

            };

            updateUICallback = function ()
            {
                $pendingDiv.addClass('hidden');
                $activeDiv.removeClass('hidden');

                var i = 0;
                updateIdx = setInterval(function()
                {
                    secondsArea.html(++i);
                }, 1000);
            };

            sendMLR(this, self, updateUICallback, revertUICallback);

            return false;
        });

    });


    function sendMLR(element, caller, updateUICallback_, revertUICallback_) {
        currentFileToSend = caller.data('file-id');

        console.log("Sending MLR");
        console.log("fileId: "+currentFileToSend);

        $.ajax({
            url: TT.urls.userAPAPI_sendMLR,
            data: {
                fileId: currentFileToSend,
            },
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
                alertify.success("MLR Sent successfully");
                revertUICallback_();
            }
        }

        function onError(data_)
        {
            $preloaderContainer.addClass('hidden');
            //alertify.alert("Message");
            alertify.alert("An error occurred :: " + data_.message);
            console.log('sendMLR.js :: onError() :: ' + data_.message);
            revertUICallback_();
        }


    }
});
