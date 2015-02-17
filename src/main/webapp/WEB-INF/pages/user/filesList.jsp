<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/user/sendFileFragment.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/user/listFiles.css" rel="stylesheet">

    <title>List Documents :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="files"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">

    <table id="listFiles" class="table table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th style="min-width: 149px">Added time</th>
            <th>Name</th>
            <th>Type</th>
            <th style="min-width: 90px">Size</th>
            <th style="min-width: 150px">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="fileTypes" type="java.util.Map<java.lang.Long,md.maxcode.si.domain.FileType>"--%>
        <%--@elvariable id="files" type="java.util.List<BaseFile>"--%>
        <%--@elvariable id="file" type="md.maxcode.si.domain.UserFile"--%>
        <c:forEach items="${files}" var="file">

            <tr class="<c:choose><c:when test="${file.validated == true}">tr-success</c:when>
                <c:when test="${file.validated == false}">tr-error warning</c:when></c:choose>">
                <td>${file.id}</td>
                <td>
                    <fmt:formatDate type="both"
                                    dateStyle="short" timeStyle="short"
                                    value="${file.addedTimeAsDate}"/>
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown">
                                <span class="glyphicon color-success
                                    <c:choose>
                                            <c:when test="${file.receivedMetadataId == null}"> color-success glyphicon-user" title="Uploaded by user"</c:when>
                                            <c:when test="${file.receivedMetadataId != null}"> color-info glyphicon-cloud-download" title="Received from SMP"</c:when>
                                        </c:choose>
                                        >
                                </span>
                                <span class="glyphicon
                                    <c:choose>
                                            <c:when test="${file.validated == true}"> color-success glyphicon-ok" title="Validated"</c:when>
                                            <c:when test="${file.validated == false}"> color-warn glyphicon-warning-sign" title="Not validated"</c:when>
                                        </c:choose>
                                        >
                                </span>
                            ${file.name} <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">

                            <li><a target="_blank"
                                   href="${pageContext.request.contextPath}/file/user/show?fileId=${file.id}">
                                <span class="glyphicon glyphicon-eye-open"></span> show <i>(in new page)</i></a></li>

                            <li><a target="_blank"
                                   href="${pageContext.request.contextPath}/file/user/download?fileId=${file.id}">
                                <span class="glyphicon glyphicon-download"></span> download</a></li>

                        </ul>
                    </div>

                    <c:if test="${file.validated == false}">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseDetails${file.id}" class="color-success">
                            (details)
                        </a>

                        <div id="collapseDetails${file.id}" class="panel-collapse collapse">
                            <div class="panel-body">
                                    ${file.validationInfo}
                            </div>
                        </div>
                    </c:if>

                </td>
                <td>${fileTypes[file.typeId].name}</td>
                <td><fmt:formatNumber type="number" maxFractionDigits="1" value="${file.size / 1000}" /> KB</td>
                <td class="action-buttons">

                    <div class="pending">
                        <div class="btn-group">
                            <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-send color-info"></span> send to <span
                                    class="caret"></span>
                            </button>
                            <ul class="dropdown-menu list" role="menu">
                                    <%--@elvariable id="apConfigs" type="java.util.List<md.maxcode.si.domain.AccessPoint>"--%>
                                    <%--@elvariable id="config" type="md.maxcode.si.domain.AccessPoint"--%>
                                <li><a href="#" data-file-id="${file.id}" data-ap-id="0" title="Loopback">
                                    <span class="glyphicon glyphicon-transfer"></span> Make a loopback</a></li>
                                <c:forEach items="${apConfigs}" var="config">
                                    <li><a href="#" data-file-id="${file.id}" data-ap-id="${config.id}"
                                           title="${config.name}">
                                        <span class="glyphicon glyphicon-cloud-upload"></span> ${config.name}</a></li>
                                </c:forEach>
                                <!--<li><a href="#" data-ap-id="0" title="Browse for more...">
                                    <span class="glyphicon glyphicon-folder-open"></span> Browse for more...</a></li>-->
                            </ul>
                        </div>

                        <button data-id="${file.id}" class="remove-btn btn btn-xs btn-link"><span
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
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/listFiles.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/sendFileToAp.js"></script>

</body>

</html>
