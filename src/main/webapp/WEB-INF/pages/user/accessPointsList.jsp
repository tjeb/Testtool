<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/user/sendFileFragment.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">

    <title>List AccessPoints :: SimplerInvoicing / TestTool</title>
</head>
<body>


<c:set var="page" scope="request" value="apConfigs"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">

    <table id="listAps" class="table table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Url</th>
            <th style="min-width: 210px">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="accessPoints" type="java.util.List<md.maxcode.si.domain.AccessPoint>"--%>
        <%--@elvariable id="accessPoint" type="md.maxcode.si.domain.AccessPoint"--%>
        <c:forEach items="${accessPoints}" var="accessPoint">

            <tr>
                <td>${accessPoint.id}</td>
                <td>${accessPoint.name}</td>
                <td><a target="_blank" title="${accessPoint.url}" href="${accessPoint.url}">${accessPoint.url}</a></td>
                <td class="action-buttons">

                    <div class="pending">
                        <div class="btn-group">
                            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-send color-info"></span> send file to this AP <span
                                    class="caret"></span>
                            </button>
                            <ul class="filesToSend dropdown-menu list" role="menu">
                                    <%--@elvariable id="files" type="java.util.List<BaseFile>"--%>
                                    <%--@elvariable id="file" type="md.maxcode.si.domain.UserFile"--%>
                                <c:forEach items="${files}" var="file">
                                    <li>
                                        <a href="#" title="${file.name}"
                                                data-ap-id="${accessPoint.id}" data-file-id="${file.id}">
                                            <span class="glyphicon glyphicon-cloud-upload"></span> ${file.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <button data-id="${accessPoint.id}" class="btn btn-xs btn-link remove-btn"><span class="glyphicon glyphicon-remove"></span> del
                        </button>

                    </div>

                    <div class="active hidden">
                        <label class="label label-info status-area"><span class="glyphicon glyphicon-time"></span> sending.. (<span class="seconds-area">0.0</span> sec)</label>
                    </div>

                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>

    <jsp:include page="fileSend_fragment.jsp" />

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/sendFileToAp.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/listAps.js"></script>

</body>
</html>
