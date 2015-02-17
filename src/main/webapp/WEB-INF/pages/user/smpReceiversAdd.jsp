<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">

    <title>Add New SMP Receiver :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="smpReceivers"/>
<jsp:include page="menu.jsp" />

<div class="container margin-top-10">

    <form id="addAPForm" class="form-horizontal col-md-7" role="form"
          enctype="multipart/form-data">

        <div class="form-group">
            <label for="participantName" class="col-sm-3 control-label">Participant name</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="participantName" id="participantName"
                       placeholder="Participant name">
                <div data-parent="participantName" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="endpoint">Choose endpoint</label>
            <div class="col-sm-7">
                <select class="form-control" id="endpoint" name="endPointId">
                    <%--@elvariable id="endpoints" type="java.util.List"--%>
                    <%--@elvariable id="endpoint" type="md.maxcode.si.domain.AccessPoint"--%>
                    <c:forEach items="${endpoints}" var="endpoint">
                        <option value="${endpoint.id}">${endpoint.name}</option>
                    </c:forEach>
                </select>

                <div data-parent="endpoint" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-7 action-buttons">
                <button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-plus"></span> Add new SMP receiver</button>
                <button type="reset" class="btn btn-link"><span class="glyphicon glyphicon-remove"></span> Reset</button>
            </div>
        </div>
    </form>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/addSMPReceiver.js"></script>

</body>
</html>
