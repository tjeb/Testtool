<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>
    <jsp:include page="commonCSS.jsp" />
    <link href="${pageContext.request.contextPath}/static/css/login.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/general.css" rel="stylesheet">

    <script type="application/javascript" src="${pageContext.request.contextPath}/static/js/jquery-2.0.3.js" ></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap.js" ></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/static/js/alertify.js" ></script>

    <script src="${pageContext.request.contextPath}/static/js/general.js"></script>
    <script>
        TT.CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/static/js/general.urls.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/static/js/popover.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/static/js/login.js"></script>
    <title>Login :: SimplerInvoicing / TestTool</title>
</head>
<body>

<div class="container">

    <form class="form-signin" role="form" name="f" method="POST" action="${pageContext.request.contextPath}/j_spring_security_check">
        <h2 style="text-align: center">
            <img src="${pageContext.request.contextPath}/static/img/SI-logo-header.png">
        </h2>
        <%--@elvariable id="error" type="java.lang.Boolean"--%>
        <c:if test="${error == true}">
            <div class="alert alert-warning alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <strong>Bad credentials!</strong> Please check credentials and try again.
            </div>
        </c:if>
        <input type="text" class="form-control" name="j_username" placeholder="Login" required autofocus>
        <input type="password" class="form-control" name="j_password" placeholder="Password" required>

        <div style="margin-bottom: 40px">
            <a id="resetPassword" class="btn-link pull-right">Forgot password ?</a>
            <a id="resetUsername" class="btn-link pull-left">Forgot username ?</a>
        </div>

        <button class="btn btn-lg btn-block" type="submit">Sign in</button>
    </form>

    <div id="resetPasswordPopover" class="popover bottom">
        <div class="arrow"></div>
        <h3 class="popover-title">
            Reset password
        </h3>

        <div class="popover-content">
            <form role="form" class="margin-top-10">
                <div class="form-group">
                    <input type="text" class="form-control" id="username"
                           name='username'
                           placeholder="Your username">
                </div>

                <div class="alert hidden" data-success="Please check your email!" ></div>

                <button class="btn btn-primary btn-submit">
                    Get temporary password
                </button>
                <button type="button" class="btn btn-link pull-right btn-close">
                    Close
                </button>

            </form>
        </div>
    </div>

    <div id="resetUsernamePopover" class="popover bottom">
        <div class="arrow"></div>
        <h3 class="popover-title">
            Your account's e-mail
        </h3>

        <div class="popover-content">
            <form role="form" class="margin-top-10">
                <div class="form-group">
                    <input type="text" class="form-control"
                           name='email' id="email" placeholder="Registered e-mail">
                </div>

                <div class="alert hidden" data-success="Please check your email!" ></div>

                <button class="btn btn-primary btn-submit">
                    Remind username
                </button>
                <button type="button" class="btn btn-link pull-right btn-close">
                    Close
                </button>

            </form>
        </div>
    </div>
</div> <!-- /container -->


</body>
</html>
