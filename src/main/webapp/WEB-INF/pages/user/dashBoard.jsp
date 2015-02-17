<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>
    <meta name="viewport" content="initial-scale = 1, user-scalable = no">

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp"/>

    <title>Dashboard :: SimplerInvoicing / TestTool</title>
</head>
<body>


<c:set var="page" scope="request" value="dashboard"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">
    <!-- Example row of columns -->

    <div class="row">
        <h2>Documents</h2>
        <table id="listFiles" class="table table-hover">
            <thead>
            <tr>
                <th>Added time</th>
                <th>Name</th>
                <th>Type</th>
            </tr>
            </thead>
            <tbody>
            <%--@elvariable id="fileTypes" type="java.util.Map<java.lang.Long,md.maxcode.si.domain.FileType>"--%>
            <%--@elvariable id="files" type="java.util.List<BaseFile>"--%>
            <%--@elvariable id="file" type="md.maxcode.si.domain.UserFile"--%>
            <c:forEach items="${files}" var="file">

                <tr class="<c:choose><c:when test="${file.validated == true}">tr-success</c:when>
                    <c:when test="${file.validated == false}">tr-error warning</c:when></c:choose>">
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
                                                <c:when test="${file.receivedMetadataId == null}"> color-success glyphicon-user"
                                          title="Uploaded by user"</c:when>
                                    <c:when test="${file.receivedMetadataId != null}"> color-info
                                          glyphicon-cloud-download" title="Received from SMP"</c:when>
                                </c:choose>
                                >
                                </span>
                                    <span class="glyphicon
                                        <c:choose>
                                                <c:when test="${file.validated == true}"> color-success glyphicon-ok"
                                          title="Validated"</c:when>
                                    <c:when test="${file.validated == false}"> color-warn
                                          glyphicon-warning-sign" title="Not validated"</c:when>
                                </c:choose>
                                >
                                </span>
                                    ${file.name} <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">

                                <li><a target="_blank"
                                       href="${pageContext.request.contextPath}/file/user/show?fileId=${file.id}">
                                    <span class="glyphicon glyphicon-eye-open"></span> show <i>(in new page)</i></a>
                                </li>

                                <li><a target="_blank"
                                       href="${pageContext.request.contextPath}/file/user/download?fileId=${file.id}">
                                    <span class="glyphicon glyphicon-download"></span> download</a></li>

                            </ul>
                        </div>
                        <c:if test="${file.validationInfo != null && !empty file.validationInfo}">
                            <div class="well well-sm">${file.validationInfo}</div>
                        </c:if>
                    </td>
                    <td>${fileTypes[file.typeId].name}</td>
                </tr>

            </c:forEach>

            </tbody>
        </table>
    </div>

    <div class="row">
        <h2>AP configs</h2>

        <table id="listAps" class="table table-hover">
            <thead>
            <tr>
                <th>Name</th>
                <th>Url</th>
            </tr>
            </thead>
            <tbody>
            <%--@elvariable id="accessPoints" type="java.util.List<md.maxcode.si.domain.AccessPoint>"--%>
            <%--@elvariable id="accessPoint" type="md.maxcode.si.domain.AccessPoint"--%>
            <c:forEach items="${accessPoints}" var="accessPoint">

                <tr>
                    <td>${accessPoint.name}</td>
                    <td><a target="_blank" title="${accessPoint.url}" href="${accessPoint.url}">${accessPoint.url}</a></td>
                </tr>

            </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp"/>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/Chart.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/dashboard.js"></script>

</body>
</html>
