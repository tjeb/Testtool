/*
 * Copyright (c) 2015 Maxcode B.V.
 */

$(document).ready(function () {

    new Popover($('#resetPassword'), '#resetPasswordPopover', TT.urls.userAPI_passwordReset);
    new Popover($('#resetUsername'), '#resetUsernamePopover', TT.urls.userAPI_usernameReset);
});
