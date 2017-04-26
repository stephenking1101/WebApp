function updateForms(tokenName, tokenValue) { 
    // 得到页面中所有的 form 元素
    var forms = document.getElementsByTagName('form'); 
    for(i=0; i<forms.length; i++) { 
        var url = forms[i].action; 

        // 如果这个 form 的 action 值为空，则不附加 csrftoken 
        if(url == null || url == "" ) continue; 

        // 动态生成 input 元素，加入到 form 之后
        var e = document.createElement("input"); 
        e.name = tokenName; 
        e.value = tokenValue; 
        e.type="hidden"; 
        forms[i].appendChild(e); 
    } 
} 

function updateTags(tokenName, tokenValue) { 
    var all = document.getElementsByTagName('a'); 
    var len = all.length; 

    // 遍历所有 a 元素
    for(var i=0; i<len; i++) { 
        var e = all[i]; 
        updateTag(e, 'href', tokenName, tokenValue); 
    } 
} 

function updateTag(element, attr, tokenName, tokenValue) { 
    var location = element.getAttribute(attr); 
    if(location != null && location != '') { 
        var fragmentIndex = location.indexOf('#'); 
        var fragment = null; 
        if(fragmentIndex != -1){ 

            //url 中含有只相当页的锚标记
            fragment = location.substring(fragmentIndex); 
            location = location.substring(0,fragmentIndex); 
        } 
		
        var index = location.indexOf('?'); 

        if(index != -1) { 
            //url 中已含有其他参数
            location = location + '&' + tokenName + '=' + tokenValue; 
        } else { 
            //url 中没有其他参数
            location = location + '?' + tokenName + '=' + tokenValue; 
        } 
        if(fragment != null){ 
            location += fragment; 
        } 
		
        element.setAttribute(attr, location); 
    } 
}

var currentMenu="";
$(document).ready(function(){
              var token = $("meta[name='_csrf']").attr("content");
              var header = $("meta[name='_csrf_header']").attr("content");
              var parameterName = $("meta[name='_csrf_parameterName']").attr("content");
              var headerJson = {'X-Requested-With':'XMLHttpRequest'};
              if(header && token) headerJson[header] = token;
			  //Default header added by jQuery
			  $.ajaxSetup({
			  	headers: headerJson
			  })
			  //updateForms(parameterName, token); 
			  //updateTags(parameterName, token); 
			  $("#loginButton").click(onAdminModalLogin);
			  $("#pass").keydown(function(e){
					                if(e.keyCode == 13) onAdminModalLogin();
					             });
			  $("#user").keydown(function(e){
				                  	if(e.keyCode == 13) onAdminModalLogin();
				                  });
			  $("#adminError").hide();
			  currentMenu = $('#currentMenu').val();
	
        	  $('#progressModal').on('shown.bs.modal', function (e) {
        		  switch(currentMenu){
        		    case "file":
        		    			hideError();
        		    			uploadFiles();
        		    			closeModelDialog();
        		    			
			                    break;
        		  }
        	  });
        	  
        	  // Every time a modal is shown, if it has an autofocus element, focus on it $('.modal'). 
        	  $('#login-modal').on('shown.bs.modal', function() {
        	    $(this).find('[autofocus]').focus();
        	  });
        	  
        	  $("#file").click( function(e) {
        		  if($("#allmap")) $("#allmap").hide();
            	  currentMenu="file";
            	  $('#currentMenu').val(currentMenu);
            	  e.preventDefault(); 
            	  $("li").removeClass("active");
            	  $(this).parent().addClass("active");
            	  if(!renderTemplate(this.id, {}, $("#content"))){
            		  return false;
            	  }
            	  window.history.pushState(currentMenu, "file upload page", "/" + currentMenu);
            	  
            	  //$('#fileForm').on('submit', uploadFiles);
            	  return false; 
                  } );

        	  //$(window).on("beforeunload", getServerTime);
        	  
        	  if(currentMenu!="" && currentMenu!="home") $("#" + currentMenu).triggerHandler("click");
        	  if(currentMenu=="maps")initMap();
});
/*
function getServerTime(){
	return "Are you sure you want to leave ?";
}
*/
function redirectPost(location, args){
            var form = $('<form></form>');
            var token = $("meta[name='_csrf']").attr("content");
            var parameterName = $("meta[name='_csrf_parameterName']").attr("content");
            var csrf = $('<input></input>');
            if(parameterName && token){
            	csrf.attr("type", "hidden");
                csrf.attr("name", parameterName);
                csrf.attr("value", token);
            }
            form.attr("method", "post");
            form.attr("action", location);
            //console.log( "redirectPost" );
            $.each( args, function( key, value ) {
                var field = $('<input></input>');
                //console.log( "RedirectPost key: ", key);
                //console.log( "RedirectPost value: ", value);
                field.attr("type", "hidden");
                field.attr("name", key);
                field.attr("value", value);
                form.append(field);
            });
            form.append(csrf);
            $(form).appendTo('body').submit();
}
    
function postAJAXRequest(url, data, template, target) {
        	//console.log("POST data: ", data);
            return $.ajax({ url:url,
                            type: 'POST',
                            contentType: 'application/json; charset=utf-8',
                            dataType: 'json',
                            data: JSON.stringify(data),
                            success: function(result,status,xhr){
                                //console.log("SUCCESS: ", result);
                                //console.log("Status: " + status);
                                //console.log("XMLHttpRequest: ", xhr);
                                renderTemplate(template, result, target);
                                //window.location.href = "http://stackoverflow.com";
                                //$(location).attr('href', window.location.protocol + "//" + window.location.host + '/home')
                                //redirectPost('home', {'X-Auth-Token': xhr.responseJSON.token});
                            },
                            error: function(xhr,status,error){
                                //console.log("data: ", data);
                                //console.log("ERROR: ", error);
                                //console.log("Status: " + status);
                                //console.log("XMLHttpRequest: ", xhr);
                                var errorMessage = getJSONValue(xhr.responseJSON, "error");
                                //console.log(errorMessage + ":SESSION_TIME_OUT");
                                if(errorMessage=="SESSION_TIME_OUT"){
                                	openErrorDialog("Your session has been timeout! Please login again.");
                                }else{
                                	renderTemplate(template, xhr.responseJSON, target);
                                }
                            },
                            async:false});
}
        
var render = new Object();
        
function renderTemplate(template, data, target) {
        	if ( !render.tmpl_cache ) { 
                render.tmpl_cache = {};
                //console.log(render.tmpl_cache);
            }
            
            if ( !render.tmpl_cache[template] ) {
                var tmpl_url = window.location.protocol + "//" + window.location.host + '/resources/template' + '/' + template + '.html';

                var tmpl_string;
                $.ajax({
                    url: tmpl_url,
                    type: 'GET',
                    async: false,
                    success: function(data) {
                    	//console.log("data: ", data);
                        tmpl_string = data;
                    },
	                error: function(xhr,status,error) {
	                	//console.log("ERROR: ", error);
                        //console.log("Status: " + status);
                        //console.log("XMLHttpRequest: ", xhr);
                        var errorMessage = getJSONValue(xhr.responseJSON, "error");
                        //console.log(errorMessage + ":SESSION_TIME_OUT");
                        if(errorMessage=="SESSION_TIME_OUT"){
                        	openErrorDialog("Your session has been timeout! Please login again.");
                        	tmpl_string = "";
                        }else{
                        	tmpl_string = errorMessage;
                        }
	                }
                });
                //console.log(tmpl_string);
                //Get the HTML from the template in the script tag and compile the template
                if(tmpl_string == "") return false;
                render.tmpl_cache[template] = Handlebars.compile(tmpl_string);
            }

            //console.log(data);
            target.html(render.tmpl_cache[template](data));
            return true;
}
        
function disableEvent(e){
                e.preventDefault();
                e.stopImmediatePropagation();
}
        
function openModelDialog(){
        	document.onkeydown = function (e) {
                return false;
            }
            //alert("submit");
            $(document).bind('click', disableEvent);

            // disable right  click
            $(document).bind('contextmenu', disableEvent);
            
            $('#myModalLabel').html('Please wait...');
            $('#progressModal').modal({backdrop: "static", keyboard: false});
}
        
function closeModelDialog(){
        	document.onkeydown = function (e) {
                return true;
            }
        	$(document).unbind('click', disableEvent);
        	$(document).unbind('contextmenu', disableEvent); // enable contextmenu
        	$('#progressModal').modal("hide");
}

function openErrorDialog(message){
	document.onkeydown = function (e) {
        return false;
    }
    //alert("submit");
    $(document).bind('click', disableEvent);

    // disable right  click
    $(document).bind('contextmenu', disableEvent);
    
    $('#errorModalLabel').html('ERROR');
    $('#errorModalBody').html(message);
    $("#errorModalButton").off("click");
    $('#errorModalButton').on('click', function(){
    	redirectPost('/sessiontimeout', {'currentMenu':currentMenu,'message':'from ajax'});
    	$('#errorModal').modal('hide');
    });
    $('#errorModal').modal({backdrop: "static", keyboard: false});
}

function getJSONValue(data, id){
	//console.log(data);
	var result = "";
	$.each(data, function(key,value){
	     if(key == id){
	    	 //console.log("Found key: ", key);
			 //console.log("Found value: ", value);
			 result = value;
	     }
	});

	return result;
}

function hideError(){
	$("#errorMessage").addClass("sr-only");
}

function onAdminModalLogin() {
	$("#adminError").hide();
	if(!$("#user").val()){
		$("#adminError").text("Please input username!");
		$("#adminError").show();
		$("#user").focus();
		return;
	}
	if(!$("#pass").val()){
		$("#adminError").text("Please input password!");
		$("#adminError").show();
		$("#pass").focus();
		return;
	}
	
	$("#pass").attr("disabled","disabled");  
	//$("#user").attr("disabled","disabled");
	$("#loginButton").attr("disabled","disabled");
	$("#loginButton").text("Loading...");

	var data={};
	data["username"] = $("#user").val();
	data["password"] = $("#pass").val();
	
	$.ajax({ url:'/admin/auth',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function(result,status,xhr){
            //console.log("SUCCESS: ", result);
            //console.log("Status: " + status);
            //console.log("XMLHttpRequest: ", xhr);
            /*var errorMessage = getJSONValue(xhr.responseJSON, "error");
            if(errorMessage){
            	$("#adminError").text(errorMessage);
        		$("#adminError").show();
        		$("#pass").removeAttr("disabled");  
            	$("#user").removeAttr("disabled");
                $("#loginButton").removeAttr("disabled");
                $("#loginButton").text("Login");
            } else {*/
            	$("#adminError").hide();
            	redirectPost('/admin', {});
            	$("#pass").removeAttr("disabled");  
            	//$("#user").removeAttr("disabled");
                $("#loginButton").removeAttr("disabled");
                $("#loginButton").text("Login");
            //}
        },
        error: function(xhr,status,error){
        	//console.log("Error: " + error);
            var errorMessage = getJSONValue(xhr.responseJSON, "error");
            var errorMessageCont = getJSONValue(xhr.responseJSON, "message");
            if(errorMessage){
            	if(errorMessage=="SESSION_TIME_OUT"){
            		$('#login-modal').modal('hide');
                	openErrorDialog("Your session has been timeout! Please login again.");
                }else if(errorMessageCont){
                	$("#adminError").text(errorMessageCont);
            		$("#adminError").show();
                }	
            }
            $("#pass").removeAttr("disabled");  
        	//$("#user").removeAttr("disabled");
            $("#loginButton").removeAttr("disabled");
            $("#loginButton").text("Login");
        },
        async:false});
}

function showPermission(event){
	var target = $(event.target);
	var userRoles = target.attr("value").split(",");
    userRoles.pop();
    var content = "";
    for(var i=0; i<userRoles.length; i++){
    	content = content + "<li>" + userRoles[i] + "</li>";
    }
    bootbox.dialog({ 
    	title: 'Your roles:',
    	message: '<div><ul>' + content + '</ul></div>',
        buttons: {
            ok: {
                label: 'OK',
                className: 'btn-info'
            }
        }
    });
}

function showCompany(event){
    bootbox.alert({ 
    	title: 'Company Info',
    	message: '<span>&copy; 2016 Superware, made with love for a better web</span>'
    });
}

function showContact(event){
	var content = '<div><p class="text-center"><em>We love our fans!</em></p>' +
	    '<div>' +
	      '<p><a href="/maps"><span class="glyphicon glyphicon-map-marker"></span>Guangzhou, Guangdong, China</a></p>' +
	      '<p><span class="glyphicon glyphicon-phone"></span>Phone: +86 20 38585116</p>' +
	      '<p><span class="glyphicon glyphicon-envelope"></span>Email: <a href="mailto:mail@mail.com">mail@mail.com</a></p>' +
	    '</div></div>';
    bootbox.alert({ 
    	title: 'Contact',
    	message: content
    });
}
