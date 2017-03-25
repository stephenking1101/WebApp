$(function() {
        BrowserDetect.init();
		//console.log("You are using " + BrowserDetect.browser + " with version " + BrowserDetect.version + ".");
		if(BrowserDetect.browser == "Explorer" && BrowserDetect.version < 9){
			//console.log("In order to use all features in the website, we suggest you to upgrade your browser.");
			//console.log("Supported browser and version are: Internet Explorer 9 or above, Chrome, Firefox, Safari and Opera");
			$('#advice').show();
		}
    	 
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

var BrowserDetect = {
        init: function () {
            this.browser = this.searchString(this.dataBrowser) || "Other";
            this.version = this.searchVersion(navigator.userAgent) || this.searchVersion(navigator.appVersion) || "Unknown";
        },
        searchString: function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                this.versionSearchString = data[i].subString;

                if (dataString.indexOf(data[i].subString) !== -1) {
                    return data[i].identity;
                }
            }
        },
        searchVersion: function (dataString) {
            var index = dataString.indexOf(this.versionSearchString);
            if (index === -1) {
                return;
            }

            var rv = dataString.indexOf("rv:");
            if (this.versionSearchString === "Trident" && rv !== -1) {
                return parseFloat(dataString.substring(rv + 3));
            } else {
                return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
            }
        },
        dataBrowser: [
            {string: navigator.userAgent, subString: "Edge", identity: "MS Edge"},
            {string: navigator.userAgent, subString: "MSIE", identity: "Explorer"},
            {string: navigator.userAgent, subString: "Trident", identity: "Explorer"},
            {string: navigator.userAgent, subString: "Firefox", identity: "Firefox"},
            {string: navigator.userAgent, subString: "Opera", identity: "Opera"},  
            {string: navigator.userAgent, subString: "OPR", identity: "Opera"},  
            {string: navigator.userAgent, subString: "Chrome", identity: "Chrome"}, 
            {string: navigator.userAgent, subString: "Safari", identity: "Safari"}       
        ]
};
