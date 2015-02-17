/*
 * Copyright (c) 2015 Maxcode B.V.
 */
function getAllAP() {

    var $ul = $('#APConfigs');
    var tmp = $ul.find('.template-APConfig');
    var liTemplate = tmp.removeClass("template-APConfig").remove()[0].outerHTML;

    $.get(TT.urls.userAPAPI_getAll, onUsersLoaded);

    function onUsersLoaded(response_) {
        var html = "";
        var items = response_.data;
        var nr = items.length;
        for (var i = 0; i < nr; i++) {
            html += TT.templateFill(liTemplate, items[i]);
        }

        $ul.append(html);
    }
};