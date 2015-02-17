<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<%@ page import="md.maxcode.si.tools.UserTypeEnum" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/highlight.css" rel="stylesheet">

    <title>Show File :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="session" value="listUsers"/>
<jsp:include page="menu.jsp"/>

<div class="container" style="margin-top: 10px">
    <!-- Example row of columns -->

    <pre>
        <code>
            ${xml}
        </code>
    </pre>


</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp"/>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/highlight.pack.js"></script>
<script>hljs.initHighlightingOnLoad();</script>
</body>
</html>
