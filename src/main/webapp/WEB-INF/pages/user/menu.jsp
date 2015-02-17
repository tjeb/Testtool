<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<%--@elvariable id="fullName" type="java.lang.String"--%>
<%--@elvariable id="userName" type="java.lang.String"--%>
<%--@elvariable id="identifier" type="java.lang.String"--%>
<%--@elvariable id="user_menu_color" type="java.lang.String"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${user_menu_color != null}">
<style>
    .user-menu-color
    {
        background-color: ${user_menu_color};
    }
</style>
</c:if>

<div class="navbar navbar-inverse navbar-default user-menu-color" role="navigation" style="border-radius: 0; border:none">

    <div class="container">
        <div class="navbar-header" style="border-radius: 0; border: none;">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/html/user/dashBoard">
                <img src="${pageContext.request.contextPath}/static/img/logo-white.png"/>
                TestTool
            </a>
        </div>

        <div class="collapse navbar-collapse navbar-left">
            <ul class="nav navbar-nav">
                <%--@elvariable id="page" type="java.lang.String"--%>
                <c:choose>
                    <c:when test="${page == 'dashboard'}">
                        <li class="active"><a><span class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/html/user/dashBoard">
                            <span class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                <c:when test="${page == 'files'}">
                <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                <li class="dropdown">
                    </c:otherwise>
                    </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-file"></span> Documents <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/files/list">
                                <span class="glyphicon glyphicon-list"></span> Show documents</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/files/add">
                                <span class="glyphicon glyphicon-cloud-upload"></span> Upload document</a>
                        </li>
                    </ul>
                </li>


                <c:choose>
                <c:when test="${page == 'apConfigs'}">
                <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                <li class="dropdown">
                    </c:otherwise>
                    </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-cog"></span> Access Points <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/accessPoints/list">
                                <span class="glyphicon glyphicon-list"></span> Show access points</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/accessPoints/add">
                                <span class="glyphicon glyphicon-plus"></span> Add new access point</a>
                        </li>
                    </ul>
                </li>

                <c:choose>
                <c:when test="${page == 'smpReceivers'}">
                <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                <li class="dropdown">
                    </c:otherwise>
                    </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-inbox"></span> SMP Receivers<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/smpReceivers/list">
                                <span class="glyphicon glyphicon-list"></span> Show added receivers</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/html/user/smpReceivers/add">
                                <span class="glyphicon glyphicon-plus"></span> Add new receiver</a>
                        </li>
                    </ul>
                </li>


            </ul>
        </div>

        <div class="navbar-right">
            <label id="settings-button" style="display: inline-block; margin-top: 15px; cursor: pointer;"
                   class="label dropdown-toggle" data-toggle="dropdown">
                 <span id="fullName">${fullName}</span> (${userName} / ${identifier})
                <span class="glyphicon glyphicon-cog"></span>
            </label>

            <div id="user-settings-popover" class="popover bottom">
                <div class="arrow"></div>
                <h3 class="popover-title">
                    Change name and password
                </h3>

                <div class="popover-content">
                    <form id="passwordForm" role="form" class="margin-top-10">
                        <div class="form-group">
                            <label for="name">Full name</label>
                            <input type="text" class="form-control" id="name"
                                   name='name' value="${fullName}"
                                   placeholder="Full name">
                        </div>
                        <div class="form-group">
                            <label for="currentPassword">Current password</label>
                            <input type="password" class="form-control" id="currentPassword"
                                   name='currentPassword'
                                   placeholder="************">
                        </div>
                        <div class="form-group">
                            <label for="newPassword">New password</label>
                            <input type="password" class="form-control" id="newPassword"
                                   name='newPassword'
                                   placeholder="New password">
                        </div>
                        <div class="form-group">
                            <label for="newPassword2">Confirm password</label>
                            <input type="password" class="form-control" id="newPassword2"
                                   name='newPassword2'
                                   placeholder="Confirm password">
                        </div>
                    </form>

                    <div class="margin-top-5">
                        <div id="settings-alert" class="alert hidden"
                             data-success="User saved with success!"
                             data-error="error">
                        </div>

                        <button id="settings-btn-submit"
                                class="btn btn-primary">Save changes
                        </button>
                        <button id="settings-btn-cancel"
                                class="btn btn-link pull-right">Close
                        </button>
                    </div>
                </div>
            </div>

            <form style="display: inline-block; padding: 0; margin-bottom: 0"
                  action="${pageContext.request.contextPath}/j_spring_security_logout">
                <button type="submit" class="btn btn-link">Log out</button>
            </form>
        </div>
        <!--/.navbar-collapse -->
    </div>

</div>