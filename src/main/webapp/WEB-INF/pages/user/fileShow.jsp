<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<%--@elvariable id="fileName" type="java.lang.String"--%>
<%--@elvariable id="xml" type="java.lang.String"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name = "viewport" content = "initial-scale = 1, user-scalable = no">

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/highlight.css" rel="stylesheet">

    <title>File - ${fileName} :: SimplerInvoicing / TestTool</title>
</head>
<body>


<c:set var="page" scope="request" value="none"/>
<jsp:include page="menu.jsp" />

<div class="container" style="margin-top: 10px">
    <!-- Example row of columns -->

    <pre>
        <label>${fileName}</label>
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
