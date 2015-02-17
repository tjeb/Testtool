<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<%--@elvariable id="admin_menu_color" type="java.lang.String"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${admin_menu_color != null}">
    <style>
        .admin-menu-color
        {
            background-color: ${admin_menu_color};
        }
    </style>
</c:if>

<div class="navbar navbar-inverse navbar-default admin-menu-color" role="navigation">

    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/html/admin/users/list">
                <img  src="${pageContext.request.contextPath}/static/img/logo-white.png"/>
                TestTool (<i>admin</i>)
            </a>
        </div>

        <div class="collapse navbar-collapse">

            <ul class="nav navbar-nav">
                <%--@elvariable id="pageName" type="java.lang.String"--%>

                <c:choose>
                    <c:when test="${pageName == 'users'}">
                        <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown">
                    </c:otherwise>
                </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-user"></span> Users <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/html/admin/users/list">
                            <span class="glyphicon glyphicon-user"></span> Show existing users</a></li>
                        <li><a href="${pageContext.request.contextPath}/html/admin/users/add">
                            <span class="glyphicon glyphicon-plus"></span> Add new user</a></li>
                    </ul>
                </li>

                <c:choose>
                    <c:when test="${pageName == 'documentTypes'}">
                        <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown">
                    </c:otherwise>
                </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-book"></span> Document types <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/html/admin/documentTypes/list">
                            <span class="glyphicon glyphicon-list"></span> Show document types</a></li>
                        <li><a href="${pageContext.request.contextPath}/html/admin/documentTypes/add">
                            <span class="glyphicon glyphicon-plus"></span> Add new document type</a></li>
                    </ul>
                </li>


            </ul>

            <div class="navbar-collapse collapse">
                <form class="navbar-form navbar-right" role="form" action="${pageContext.request.contextPath}/j_spring_security_logout">
                    <%--@elvariable id="fullName" type="java.lang.String"--%>
                    <%--@elvariable id="userName" type="java.lang.String"--%>
                    <label class="label"><span class="glyphicon glyphicon-user"></span> ${fullName} (${userName})</label>
                    <button type="submit" class="btn btn-link">Log out <span class="glyphicon glyphicon-remove"></span> </button>
                </form>
            </div>
        </div>

        <!--/.navbar-collapse -->
    </div>

</div>