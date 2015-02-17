<%@ page import="md.maxcode.si.tools.UserTypeEnum" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">

    <title>List Users :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="session" value="listUsers"/>
<jsp:include page="menu.jsp"/>

<div class="container" style="margin-top: 10px">
    <!-- Example row of columns -->

    <table id="userTable" class="table table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th>Full name</th>
            <th>Company name</th>
            <th>Username</th>
            <th>E-mail</th>
            <th>Password</th>
            <th>Identifier</th>
            <th>Type</th>
            <th>Enabled</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>

        <tr class="template-element interactive-edit" data-id="#[id]">
            <td>#[id]</td>

            <td><input type="text" name="name" value="#[name]" data-bak-value="#[name]" title="#[name]"></td>

            <td><input type="text" name="companyName" value="#[companyName]" data-bak-value="#[companyName]"
                       title="#[companyName]">
            </td>

            <td><input type="text" name="username" value="#[username]" data-bak-value="#[username]" title="#[username]">
            </td>

            <td><input type="email" name="email" value="#[email]" data-bak-value="#[email]" title="#[email]">
            </td>

            <td><input type="password" name="password" value="" data-bak-value="" title="Password" placeholder="*******">
            </td>

            <td><input type="text" name="identifier" value="#[identifier]" data-bak-value="#[identifier]"
                       title="#[identifier]">
            </td>

            <td>
                <select name="type" data-value="#[type]" data-bak-value="#[type]">
                    <option><%= UserTypeEnum.lite %>
                    </option>
                    <option><%= UserTypeEnum.full %>
                    </option>
                </select>
            </td>

            <td>
                <select name="enabled" data-value="#[enabled]" data-bak-value="#[enabled]">
                    <option><%= Boolean.TRUE %>
                    </option>
                    <option><%= Boolean.FALSE %>
                    </option>
                </select>
            </td>

            <td class="action-buttons passive">
                <button class="btn btn-xs btn-success disabled save-btn"><span
                        class="glyphicon glyphicon-ok"></span> save
                </button>

                <button class="btn btn-xs btn-default disabled revert-btn"><span
                        class="glyphicon glyphicon-flash"></span> reset
                </button>

                <span class="spacer-10px"></span>

                <button class="btn btn-xs btn-link del-btn"><span class="glyphicon glyphicon-remove"></span> del
                </button>
            </td>
        </tr>

        </tbody>
    </table>


</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp"/>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/admin/listUsers.js"></script>

</body>
</html>
