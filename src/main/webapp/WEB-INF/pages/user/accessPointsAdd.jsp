<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">

    <title>Add New AccessPoint :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="apConfigs"/>
<jsp:include page="menu.jsp" />

<div class="container margin-top-10">

    <form id="addAPForm" class="form-horizontal col-md-7" role="form"
          enctype="multipart/form-data">

        <div class="form-group">
            <label for="name" class="col-sm-3 control-label">Name</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="name" id="name" placeholder="Unique Name">
                <div data-parent="name" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="description" class="col-sm-3 control-label">Description</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="description" id="description" placeholder="Description">
                <div data-parent="description" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="certificate" class="col-sm-3 control-label">Certificate content</label>
            <div class="col-sm-7">
                <textarea class="form-control" name="certificate" id="certificate" placeholder="Certificate content"
                         style="height: 200px"></textarea>
                <div data-parent="description" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="url" class="col-sm-3 control-label">Endpoint URL</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="url" id="url" placeholder="http://endpoint.url">
                <div data-parent="url" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="technicalUrl" class="col-sm-3 control-label">Information url</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" name="technicalUrl" id="technicalUrl" placeholder="Technical information url">
                <div data-parent="technicalUrl" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="contactEmail" class="col-sm-3 control-label">Contact e-mail</label>
            <div class="col-sm-7">
                <input type="email" class="form-control" name="contactEmail" id="contactEmail" placeholder="Contact e-mail">
                <div data-parent="contactEmail" class="margin-top-10 alert alert-warning input-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-7 action-buttons">
                <button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-plus"></span> Add new access point</button>
                <button type="reset" class="btn btn-link"><span class="glyphicon glyphicon-remove"></span> Reset</button>
            </div>
        </div>

        <div id="successAlert" class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <strong>Success!</strong>
            <a href="${pageContext.request.contextPath}/html/user/accessPoints/list" class="btn btn-xs btn-link" title="Open Access Points overview page >">
                <span class="glyphicon glyphicon-link"></span> Open Access Points overview page >
            </a>
        </div>

    </form>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/user/addAPConfig.js"></script>

</body>
</html>
