<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2015 Maxcode B.V.
  --%>

<html>
<head>

    <jsp:include page="/WEB-INF/pages/commonCSS.jsp" />

    <title>Add New Document Type :: SimplerInvoicing / TestTool</title>
</head>
<body>

<c:set var="page" scope="request" value="documentTypes"/>
<jsp:include page="menu.jsp"/>

<div class="container margin-top-10">
    <!-- Example row of columns -->

    <form id="addDocumentType" class="col-md-6 form-horizontal" role="form">

        <div class="form-group">
            <label class="col-sm-5 control-label" for="galaxyMetadataProfile">Choose PEPPOL Profile</label>
            <div class="col-sm-7">
                <select class="form-control" id="galaxyMetadataProfile" name="galaxyMetadataProfile">
                    <option>None</option>
                    <%--@elvariable id="metadataProfiles" type="java.util.List"--%>
                    <%--@elvariable id="profile" type="md.maxcode.si.smp.galaxygateway.GMetadataProfile"--%>
                    <c:forEach items="${metadataProfiles}" var="profile">
                        <option data-identifier-id="${profile.documentIdentifier.value}" value="${profile.base64}"
                                title="${profile.documentIdentifier.scheme} / ${profile.documentIdentifier.value}">${profile.commonName}</option>
                    </c:forEach>
                </select>

                <div data-parent="galaxyMetadataProfile" class="margin-top-10 alert alert-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-5 control-label" for="name">Name</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="name" name="name" placeholder="Unique Name">

                <div data-parent="name" class="margin-top-10 alert alert-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-5 control-label" for="identifier">Identifier</label>
            <div class="col-sm-7">
                <textarea class="form-control" style="height: 150px" id="identifier" name="identifier" placeholder="Unique Identifier">

                </textarea>

                <div data-parent="identifier" class="margin-top-10 alert alert-warning">
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-5 col-sm-7 action-buttons">
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-plus"></span> Add new document type
                </button>
                <button type="reset" class="btn btn-link">
                    <span class="glyphicon glyphicon-remove"></span> Reset
                </button>
            </div>
        </div>
    </form>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/admin/addDocumentType.js"></script>

</body>
</html>
