<%@ page import="md.maxcode.si.tools.TTSettings" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/user/sendFileFragment.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/user/listFiles.css" rel="stylesheet">

    <title>List SMP Receivers :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="smpReceivers"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">

    <table id="listSMPReceivers" class="table table-hover">
        <thead>
        <tr>
            <th>Name</th>
            <th>Identifier</th>
            <th>SML Activation</th>
            <th style="min-width: 150px">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="identifiers" type="java.util.List"--%>
        <%--@elvariable id="identifier" type="md.maxcode.si.smp.galaxygateway.GParticipantIdentifier"--%>
        <c:forEach items="${identifiers}" var="identifier">

            <tr>
                <td title="${identifier.name}">${identifier.name}</td>
                <td title="${identifier.value}">${identifier.value}</td>
                <td>${identifier.smlActivation}</td>
                <td class="action-buttons">

                    <div class="pending">
                        <div class="btn-group">
                            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-send color-info"></span> send file to this receiver <span
                                    class="caret"></span>
                            </button>
                            <ul class="dropdown-menu list" role="menu">
                                    <%--@elvariable id="files" type="java.util.List"--%>
                                    <%--@elvariable id="file" type="md.maxcode.si.domain.UserFile"--%>
                                <c:forEach items="${files}" var="file">
                                    <li>
                                        <a href="#" title="${file.name}"  data-identifier="${identifier.value}"
                                           data-ap-id="-1" data-file-id="${file.id}">
                                            <span class="glyphicon glyphicon-cloud-upload"></span> ${file.name}</a>
                                    </li>
                                </c:forEach>
                                <!--<li><a href="#" data-ap-id="0" title="Browse for more...">
                                    <span class="glyphicon glyphicon-folder-open"></span> Browse for more...</a></li>-->
                            </ul>
                        </div>

                        <button data-value="${identifier.value}" class="remove-btn btn btn-xs btn-link"><span
                                class="glyphicon glyphicon-remove"></span> del
                        </button>
                    </div>

                    <div class="active hidden">
                        <label class="label label-info status-area"><span
                                class="glyphicon glyphicon-time"></span> sending.. (<span
                                class="seconds-area">0.0</span> sec)</label>
                    </div>

                </td>
            </tr>

        </c:forEach>

        </tbody>
    </table>

    <jsp:include page="fileSend_fragment.jsp"/>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp"/>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/sendFileToAp.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/listSMPReceiver.js"></script>

</body>

</html>
