<%@ page import="md.maxcode.si.tools.UserTypeEnum" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">

    <title>Add New User :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="addUsers"/>
<jsp:include page="menu.jsp" />

<div class="container margin-top-10">
    <!-- Example row of columns -->

    <div class="col-md-6">

        <form id="addUsers" class="form-horizontal" role="form">

            <div class="form-group">
                <label for="identifier" class="col-sm-3 control-label">Identifier</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="identifier" id="identifier" placeholder="Identifier">

                    <div data-parent="identifier" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="username1" class="col-sm-3 control-label">Username</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="username" id="username1" placeholder="Username" autocomplete="off">

                    <div data-parent="username" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>

            <div class="form-group hidden">
                <input type="text" class="form-control" id="username" placeholder="Username" autocomplete="off">
            </div>
            <div class="form-group">
                <label for="password" class="col-sm-3 control-label">Temp. password</label>
                <div class="col-sm-7">
                    <input type="password" class="form-control" name="password" id="password" placeholder="Temp. password">

                    <div data-parent="newPassword" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-3 control-label">Full name</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="name" id="name" placeholder="Full name">

                    <div data-parent="name" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="companyName" class="col-sm-3 control-label">Company name</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="companyName" id="companyName" placeholder="Company name">

                    <div data-parent="companyName" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="email" class="col-sm-3 control-label">E-mail</label>
                <div class="col-sm-7">
                    <input type="email" class="form-control" name="email" id="email" placeholder="email">

                    <div data-parent="email" class="margin-top-10 alert alert-warning">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="type" class="col-sm-3 control-label">User type</label>
                <div class="col-sm-7">
                    <select id="type" name="type" class="form-control">
                        <option><%= UserTypeEnum.lite %></option>
                        <option><%= UserTypeEnum.full %></option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="enabled" class="col-sm-3 control-label">Enabled</label>
                <div class="col-sm-7">
                    <select id="enabled" name="enabled" class="form-control">
                        <option><%= Boolean.TRUE %></option>
                        <option><%= Boolean.FALSE %></option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-7 action-buttons">
                    <button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-plus"></span> Add new user</button>
                    <button type="reset" class="btn btn-link"><span class="glyphicon glyphicon-remove"></span> Reset</button>
                </div>
            </div>
        </form>

    </div>



</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/admin/addUsers.js" ></script>

</body>
</html>
