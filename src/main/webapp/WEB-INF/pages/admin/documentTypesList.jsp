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

    <title>List Document Types :: SimplerInvoicing / TestTool</title>
</head>
<body>

<div class="preloader-container hidden">

    <div class="row">
        <div class="col-xs-6 col-md-4"></div>
        <div class="col-xs-6 col-md-4">

            <div class="well well-sm well-preloader">
                Working, hold on..
            </div>

        </div>
        <div class="col-xs-6 col-md-4"></div>
    </div>
</div>

<c:set var="page" scope="request" value="documentTypes"/>
<jsp:include page="menu.jsp"/>

<div class="container" style="margin-top: 10px">
    <!-- Example row of columns -->

    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12">
            <table id="documentTypesTable" class="table table-hover">
                <thead>
                <tr>
                    <th style="min-width: 25px">Id</th>
                    <th style="min-width: 180px;">Name</th>
                    <th style="min-width: 180px;">Identifier</th>
                    <th>Attached files</th>
                    <th style="min-width: 250px;">Actions</th>
                </tr>
                </thead>
                <tbody>

                <tr class="template-element interactive-edit" data-id="#[id]">
                    <td>#[id]</td>
                    <td><input type="text" name="name" value="#[name]" data-bak-value="#[name]" title="#[name]"></td>
                    <td><input type="text" name="identifier" value="#[identifier]" data-bak-value="#[identifier]" title="#[identifier]"></td>
                    <td id="attachments-#[id]">
                        $[template-attached-file]
                    </td>
                    <td class="action-buttons passive">
                        <button class="btn btn-xs btn-info add-file-btn"
                                data-type-id="#[id]"><span
                                class="glyphicon glyphicon-plus"></span> file
                        </button>
                        <button class="spacer-20px btn btn-xs btn-success disabled save-btn"><span
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

            <div id="attachment-#[id]" class="template-attached-file btn-group spacer-5px spacer-top-5px #[extension] #[marked#?true#:marked]">
                <button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown">
                    #[name] <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="mark-attachment" href="#" data-parent-id="attachment-#[id]" data-id="#[id]">
                        <span class="glyphicon glyphicon-ok"></span> mark as root file</a></li>
                    <li><a class="show-attachment" target="_blank" href="${pageContext.request.contextPath}/file/admin/show?id=#[id]"
                           data-id="#[id]"><span class="glyphicon glyphicon-eye-open"></span> show <i>(in new page)</i></a></li>
                    <li><a class="delete-attachment" href="#" data-parent-id="attachment-#[id]" data-id="#[id]">
                        <span class="glyphicon glyphicon-remove"></span> delete attachment</a></li>
                </ul>
            </div>

        </div>
    </div>



    <form id="uploadForm" class="hidden" role="form" enctype="multipart/form-data">
        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" class="form-control" id="name" name="name">
        </div>
        <div class="hidden">
            <input id="fileSelect" name="file" type="file"/>
        </div>
    </form>

</div>

<jsp:include page="/WEB-INF/pages/commonJS.jsp" />
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/admin/AttachmentUploader.js"></script>
<script type="application/javascript" src="${pageContext.request.contextPath}/static/js/admin/listDocumentTypes.js"></script>

</body>
</html>
