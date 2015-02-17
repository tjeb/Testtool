<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />

    <title>Upload Document :: SimplerInvoicing / TestTool</title>
</head>
<body>

<div class="preloader-container hidden">

    <div class="row">
        <div class="col-xs-6 col-md-4"></div>
        <div class="col-xs-6 col-md-4">

            <div class="well well-sm well-preloader">
                File is being uploaded and validated, hold on..
            </div>

        </div>
        <div class="col-xs-6 col-md-4"></div>
    </div>
</div>

<c:set var="page" scope="request" value="files"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">
    <!-- Example row of columns -->

    <form id="uploadForm" class="form-horizontal col-md-7" role="form">

        <div class="form-group">
            <label class="control-label col-sm-3" for="name">Document</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="name" name="name" placeholder="Click here to browse">
            </div>
        </div>
        <div class="hidden">
            <input id="fileSelect" name="file" type="file"/>
        </div>

        <div class="form-group">
            <label class="control-label col-sm-3" for="name">Type</label>

            <div class="col-sm-7">
                <select id="type" name="type" class="form-control">
                    <%--@elvariable id="fileTypes" type="java.util.List<md.maxcode.si.domain.UserFile>"--%>
                    <%--@elvariable id="item" type="md.maxcode.si.domain.FileType"--%>
                    <c:forEach items="${fileTypes}" var="item">
                        <option data-id="${item.id}">${item.name}</option>
                    </c:forEach>

                </select>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-7 action-buttons">
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-cloud-upload"></span> Upload new document
                </button>
                <button type="reset" class="btn btn-link">
                    <span class="glyphicon glyphicon-remove"></span> Reset
                </button>
            </div>
        </div>

        <div id="errorDetailsAlert" class="alert alert-danger spacer-top-5px hidden">
            <strong>An error occured!</strong>
            <p>
                ..
            </p>
        </div>

        <div id="warningDetailsAlert" class="alert alert-warning spacer-top-5px hidden">
            <strong>File was uploaded, but some warning messages are present!</strong>
            <p>
                ..
            </p>
        </div>
    </form>


</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/addFile.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/FileUploader.js"></script>

</body>
</html>
