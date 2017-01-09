$(function() {
    	 
        if (localStorage.chkbx && localStorage.chkbx != '') {
            $('#rememberMe').attr('checked', 'checked');
            $('#inputUser').val(localStorage.usr);
            $('#inputPassword').val(sjcl.decrypt("application", localStorage.pass));
        } else {
            $('#rememberMe').removeAttr('checked');
            $('#inputUser').val('');
            $('#inputPassword').val('');
        }
        
        //Read from cookies
        //var remember = $.cookie('remember');
        //if ( remember == 'true' ) {
        //var username = $.cookie('username');
        //var password = $.cookie('password');
        // autofill the fields
        //$('#username').attr("value", username);
        //$('#password').attr("value", password);

        $('#rememberMe').click(rememberMe);
    });
    
function rememberMe(){
            if ($('#rememberMe').is(':checked')) {
                // save username and password
                localStorage.usr = $('#inputUser').val();
                localStorage.pass = sjcl.encrypt("application", $('#inputPassword').val());
                localStorage.chkbx = $('#rememberMe').val();
            } else {
                localStorage.usr = '';
                localStorage.pass = '';
                localStorage.chkbx = '';
            }
            //if ($('#remember').attr('checked')) {
                //var username = $('#username').attr("value");
                //var password = $('#password').attr("value");
                // set cookies to expire in 14 days
                //$.cookie('username', username, { expires: 14 });
                //$.cookie('password', password, { expires: 14 });
                //$.cookie('remember', true, { expires: 14 });
                //} else {
                // reset cookies
                //$.cookie('username', null);
                //$.cookie('password', null);
                //$.cookie('remember', null);
                //}
}
    
function openModelDialog(){
            document.onkeydown = function (e) {
                return false;
            }
            //alert("submit");
            $(document).click(function(e) {
                //e.stopPropagation();
                e.preventDefault();
                e.stopImmediatePropagation();
            });

            // disable right  click
            $(document).bind('contextmenu', function(e) {
                //e.stopPropagation();
                e.preventDefault();
                e.stopImmediatePropagation();
            });
            
            $('#myModalLabel').html('Please wait...');
            $('#progressModal').modal({backdrop: "static", keyboard: false});
            rememberMe();
}