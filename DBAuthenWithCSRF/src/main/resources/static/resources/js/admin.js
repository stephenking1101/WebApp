Handlebars.registerHelper('ifCond', function(v1, v2, options) {
  if(v1 == v2) {
    return options.fn(this);
  }
  
  return options.inverse(this);
});

Handlebars.registerHelper('listFirstThree', function(context, options){
	var ret="";
	if(context){
		var j=context.length;
		var end="";
		if(j > 3) {
			j=3;
			end="..."
		}
		
		for(var i=0; i<j; i++){
			if(i==j-1){
				ret = ret + options.fn(context[i]) + end;
			} else {
				ret = ret + options.fn(context[i]) + ", ";
			}
		}
	}
	
	return ret;
});

Handlebars.registerHelper('pagination', function(totalPages, currentPage) {
    var result=""; 
	if(totalPages > 1){
		  if(currentPage==1){
			  result='<ul class="pagination"><li class="page-pre disabled"><a href="javascript:void(0)">Previous</a></li>';  
		  }else{
			  result='<ul class="pagination"><li class="page-pre"><a href="javascript:void(0)" onclick="showPage('+ (currentPage-1) +');">Previous</a></li>';
		  }
		  
		  for(var i=0; i<totalPages; i++){
			var pageNum = i+1;
			if(pageNum==currentPage){
				result=result + '<li class="page-number active"><a href="javascript:void(0)" onclick="showPage('+ pageNum +');">'
				+ pageNum
				+ '</a></li>';
			} else {
				result=result + '<li class="page-number"><a href="javascript:void(0)" onclick="showPage('+ pageNum +');">'
				+ pageNum
				+ '</a></li>';
			}
		  }
		  if(currentPage==totalPages){
			  result=result+'<li class="page-next disabled"><a href="javascript:void(0)">Next</a></li></ul>';
		  }else{
			  result=result+'<li class="page-next"><a href="javascript:void(0)" onclick="showPage('+ (currentPage+1) +');">Next</a></li></ul>';  
		  }
	}

	return new Handlebars.SafeString(result);
});

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
			  //Default header added by jQuery
              var headerJson = {'X-Requested-With':'XMLHttpRequest'};
              headerJson[header] = token;
			  $.ajaxSetup({
				  headers: headerJson
			  })
			  currentMenu = $('#currentMenu').val();
	
        	  $('#progressModal').on('shown.bs.modal', function (e) {
        		  switch(currentMenu){
        		    case "admin/users":
        		    		hideError();
        		    		var currentPage = $('#currentPage').val();
        					if(currentPage){
        						currentPage=currentPage-1;
        					} else {
        						currentPage=0;
        					}
			                postAJAXRequest("/admin/" + "users/list?page=" + currentPage + "&size=2", {"username":$("#userName").val()}, currentMenu, $("#content"));
			                    
			                //renderTemplate(currentMenu, JSON.parse('{"error":"Please input a user name!"}'), $("#content"));
			                closeModelDialog();
			                //$(".userRemove").click(onRemoveButtonClick);
			                break;
        		    case "admin/roles":
			    			hideError();
			    			postAJAXRequest("/admin/" + "roles", {}, currentMenu, $("#content"));
		                    closeModelDialog();
		                    break;
        		  }
        	  });
        		
              $("#admin\\/users").click( function(e) {
            	  currentMenu="admin/users";
            	  $('#currentMenu').val(currentMenu);
            	  e.preventDefault(); 
            	  
            	  $("li").removeClass("active");
            	  $(this).parent().addClass("active");
            	  renderTemplate(this.id, {}, $("#content"));
            	  window.history.pushState(currentMenu, "users page", "/" + currentMenu);
            	  openModelDialog();
            	  if(lbd.misc.navbar_menu_visible == 1) $('.navbar-toggle').triggerHandler("click");
            	  return false; 
                  } );
        	  
        	  $("#admin\\/roles").click( function(e) {
            	  currentMenu="admin/roles";
            	  $('#currentMenu').val(currentMenu);
            	  e.preventDefault(); 
            	  
            	  $("li").removeClass("active");
            	  $(this).parent().addClass("active");
            	  renderTemplate(this.id, {}, $("#content"));
            	  window.history.pushState(currentMenu, "roles page", "/" + currentMenu);
            	  openModelDialog();
            	  if(lbd.misc.navbar_menu_visible == 1) $('.navbar-toggle').triggerHandler("click");
            	  return false; 
                  } );
        	  
        	  if(currentMenu!="" && currentMenu!="home") $("#" + currentMenu.replace("/", "\\/")).triggerHandler("click");
});

function redirectPost(location, args){
			var form = $('<form></form>');
		    var token = $("meta[name='_csrf']").attr("content");
		    var parameterName = $("meta[name='_csrf_parameterName']").attr("content");
		    var csrf = $('<input></input>');
		    csrf.attr("type", "hidden");
		    csrf.attr("name", parameterName);
		    csrf.attr("value", token);
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
        	console.log("POST data: ", data);
            return $.ajax({ url:url,
                            type: 'POST',
                            contentType: 'application/json; charset=utf-8',
                            dataType: 'json',
                            data: JSON.stringify(data),
                            success: function(result,status,xhr){
                                //console.log("SUCCESS: ", result);
                                //console.log("Status: " + status);
                                //console.log("XMLHttpRequest: ", xhr);
                                if(!template){
                                	//console.log("return");
                                	return;
                                } else if($.isFunction(template)){
                                	template(null, null, result);
                                } else if(target){
                                	//console.log("has target");
                                	renderTemplate(template, result, target);
                                }
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
                                var errorMessageCont = getJSONValue(xhr.responseJSON, "message");
                                //console.log(errorMessage + ":SESSION_TIME_OUT");
                                if(errorMessage=="SESSION_TIME_OUT"){
                                	$("div.modal").each(function(){
                                	     $(this).modal('hide');
                                	  });
                                	openErrorDialog("Your session has been timeout! Please login again.");
                                }else{
                                	if(!template){
                                    	//console.log("return");
                                    	return;
                                    } else if($.isFunction(template)){
                                    	template(errorMessage, errorMessageCont);
                                    } else if(target){
                                    	//console.log("has target");
                                    	renderTemplate(template, xhr.responseJSON, target);
                                    }
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
                render.tmpl_cache[template] = Handlebars.compile(tmpl_string);
            }

            //console.log(data);
            target.html(render.tmpl_cache[template](data)); 
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
            $('#progressModal').modal({backdrop: false, keyboard: false});
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
    $('#errorModalButton').on('click', function(){
    	redirectPost('/sessiontimeout', {'currentMenu':currentMenu,'message':'from ajax'});
    });
    $('#errorModal').modal({backdrop: false, keyboard: false});
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

function showNotification(from, align, type, msg){
    $.notify({
        icon: "pe-7s-note2",
        message: msg
        
    },{
        type: type,
        delay: 5,
        timer: 3000,
        placement: {
            from: from,
            align: align
        }
    });
}

function showAccountInfo(event){
	var target = $(event.target);
	var userRoles = target.attr("value").split(",");
    userRoles.pop();
    var content = "";
    for(var i=0; i<userRoles.length; i++){
    	content = content + "<li>" + userRoles[i] + "</li>";
    }
    
    var message = '<div><ul>' + content + '</ul></div>';
    $('#infoModalLabel').html('Your roles:');
    $('#infoModalBody').html(message);
    $('#infoModal').modal({backdrop: false, keyboard: false});
}
