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

    var $sendPopover = $('#sendPopover');
    var $sendPopoverTitle = $sendPopover.find('.popover-title');
    var $senderInput = $sendPopover.find('#senderPeppolId');
    var $recipientInput = $sendPopover.find('#recipientPeppolId');
    var $generateTriggerUrl = $sendPopover.find('#generateTriggerUrl');
    var $popOverArrow = $sendPopover.find('.arrow');
    var defaultArrowTop = parseInt($popOverArrow.css("top").replace('px', ''));

    $sendPopover.find('.btn-send').click(function()
    {
        $sendPopover.hide();
        updateUICallback();
        sendFileToAp(revertUICallback);
    });

    $sendPopover.find('.btn-cancel').click(function()
    {
        $sendPopover.hide();
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

        $actionButtons.find('li a').on('click', function () {
            var self = $(this);

            currentFileToSend = self.data('file-id');
            currentAP_id = self.data('ap-id');
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

            var pointArr = TT.getElementPosition($actionButtons[0]);
            var y = pointArr[1];
            var delta = window.innerHeight - pointArr[1];
            var arrow_y = defaultArrowTop;

            if(delta < $sendPopover.height())
            {
                y = window.innerHeight - $sendPopover.height();
                arrow_y += pointArr[1] - y;
            }

            $sendPopover.css('left', pointArr[0] - $sendPopover.width());
            $sendPopover.css('top', y);
            $popOverArrow.css('top', arrow_y);

            var fileName = self.attr('title');
            $sendPopoverTitle.html(TT.sliceWord(fileName, 20));
            $sendPopoverTitle.attr('title', fileName);

            var defaultRecipientId = self.data('identifier');
            if(typeof defaultRecipientId != "undefined" && defaultRecipientId != '')
            {
                $recipientInput.val(defaultRecipientId);
            }

            $sendPopover.show();

            return false;
        });

    });


    function sendFileToAp(revertUICallback_)
    {
        var generateUrl = $generateTriggerUrl[0].checked;

        if(generateUrl)
        {
            $preloaderDialog.addClass('hidden');
            $preloaderMessage.removeClass('hidden');
            $preloaderContainer.removeClass('hidden');
        }

        console.log("apConfigId: "+currentAP_id);
        console.log("fileId: "+currentFileToSend);
        console.log("senderPeppolId: "+$senderInput.val());
        console.log("recipientPeppolId: "+$recipientInput.val());
        console.log("generateTriggerUrl: "+generateUrl);

        $.ajax({
            url: TT.urls.userAPAPI_sendFile,
            data: {
                apConfigId: currentAP_id,
                fileId: currentFileToSend,
                senderPeppolId: $senderInput.val(),
                recipientPeppolId: $recipientInput.val(),
                generateTriggerUrl: generateUrl
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
                if(generateUrl)
                {
                    $preloaderUrlArea.html(data_.data);
                    $preloaderDialog.removeClass('hidden');
                    $preloaderMessage.addClass('hidden');
                }
                alertify.success(generateUrl ? "URL generated successfully" : "File sent successfully");
                revertUICallback_();
            }
        }

        function onError(data_)
        {
            $preloaderContainer.addClass('hidden');
            //alertify.alert("Message");
            alertify.alert("An error occurred :: " + data_.message);
            console.log('sendFileToAp.js :: onError() :: ' + data_.message);
            revertUICallback_();
        }


    }

});