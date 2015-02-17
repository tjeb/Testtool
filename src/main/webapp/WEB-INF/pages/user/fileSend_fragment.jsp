<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<%--@elvariable id="defaultSenderId" type="java.lang.String"--%>
<%--@elvariable id="defaultRecipientId" type="java.lang.String"--%>
<div id="sendPopover" class="popover left" style="position: fixed">
    <div class="arrow"></div>
    <h3 class="popover-title">file to send</h3>
    <div class="popover-content col-sm-12">

        <div class="input-group input-group-sm  margin-top-10" title="Sender's Peppol Id">
            <span class="input-group-addon"><span class="glyphicon glyphicon-export"></span></span>
            <input type="text" disabled class="form-control input-sm"
                   id="senderPeppolId"
                   placeholder="Sender" value="${defaultSenderId}">
        </div>
        <div class="input-group input-group-sm  margin-top-10" title="Recipient's Peppol Id">
            <span class="input-group-addon"><span class="glyphicon glyphicon-saved"></span></span>
            <input type="text" class="form-control input-sm"
                   id="recipientPeppolId"
                   placeholder="Recipient" value="${defaultRecipientId}">
        </div>
        <div class="radio">
            <label>
                <input type="radio" name="generateTriggerUrl" value="1" checked>
                Send document now
            </label>
        </div>
        <div class="radio">
            <label>
                <input id="generateTriggerUrl" type="radio" name="generateTriggerUrl" value="2">
                Generate triggering url
            </label>
        </div>

        <button class="btn btn-info btn-xs margin-top-10 btn-send">
            <span class="glyphicon glyphicon-ok"></span> submit
        </button>
        <button class="btn btn-link btn-xs margin-top-10 btn-cancel pull-right">
            cancel <span class="glyphicon glyphicon-remove"></span>
        </button>
    </div>
</div>


<div  class="preloader-container hidden">

    <div class="row">
        <div class="col-xs-1 col-md-4 col-sm-1"></div>
        <div class="col-xs-10 col-md-4 col-sm-10">

            <div class="hidden well well-sm well-preloader">
                Working, hold on..
            </div>

            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">URL to trigger document sending</h4>
                    </div>
                    <div class="modal-body">
                        <textarea id="urlArea">http://ceva.md</textarea>
                    </div>
                </div>
            </div>

        </div>
        <div class="col-xs-1 col-md-4 col-sm-1"></div>
    </div>

</div>