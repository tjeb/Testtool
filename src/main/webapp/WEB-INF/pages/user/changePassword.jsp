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

    <title>Change Password :: SimplerInvoicing / TestTool</title>
</head>
<body>

<div class="container">

    <div class="row">

        <div style="text-align: center">
            <img src="${pageContext.request.contextPath}/static/img/SI-logo-header.png">
        </div>

        <form class="col-sm-4 col-sm-offset-4" role="form" style="margin-top: 30px"
              action="${pageContext.request.contextPath}/html/user/changePassword"
              method="POST">
            <div class="form-group">
                <label for="newPassword">New password</label>
                <input type="password" class="form-control" id="newPassword"
                       name='newPassword'
                       placeholder="New password">
                <c:if test="${errors.newPassword != null}">
                    <div class="alert">
                            ${errors.newPassword}
                    </div>
                </c:if>
            </div>
            <div class="form-group">
                <label for="newPassword2">Confirm password</label>
                <input type="password" class="form-control" id="newPassword2"
                       name='newPassword2'
                       placeholder="Confirm password">
                <%--@elvariable id="errors" type="java.util.Map"--%>
                <c:if test="${errors.newPassword2 != null}">
                    <div class="alert">
                            ${errors.newPassword2}
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <input type="submit" value="Submit">
            </div>
        </form>
        <form class="col-sm-4 col-sm-offset-4"
              action="${pageContext.request.contextPath}/j_spring_security_logout">
            <button type="submit" class="btn btn-link" style="width: 100%">Log out</button>
        </form>
    </div>

</div>

</body>
</html>
