$(document).ready(function(){
	$.ajax({
   	    url: 'data/product.json',
	   	type: 'GET',
	    contentType: 'application/json; charset=utf-8',
	    dataType: 'json',
	    //data: JSON.stringify({}),
	    success: function(result,status,xhr){
	        if(renderTemplate("products", result, $("#flash_templates"))){
		        init_masonry();	
	        }
	    },
	    error: function(xhr,status,error){
            var errorMessage = getJSONValue(xhr.responseJSON, "error");
            var errorMessageCont = getJSONValue(xhr.responseJSON, "message");
            if(errorMessageCont){
                openErrorBootBox(errorMessageCont);   
            }else if(errorMessage){
                openErrorBootBox(errorMessage);
            }
	    },
	    async:false
   	});
});

function init_masonry(){
	var $container = $('.item_container');
	$container.imagesLoaded( function(){
		$container.masonry({
			itemSelector : '.item',
			gutter: 18,
			originLeft: true,
			fitWidth: true
		});
	});
}

var render = new Object();
function renderTemplate(template, data, target) {
	if ( !render.tmpl_cache ) { 
        render.tmpl_cache = {};
    }
    if ( !render.tmpl_cache[template] ) {
    	var pathArray = window.location.pathname.split( '/' );
        var tmpl_url = window.location.protocol + "//" + window.location.host + '/template' + '/' + template + '.html';
        if(pathArray.length > 2) tmpl_url = window.location.protocol + "//" + window.location.host + '/' + pathArray[1] + '/template' + '/' + template + '.html';
        var tmpl_string="";
        $.ajax({
            url: tmpl_url,
            type: 'GET',
            async: false,
            success: function(data) {
                tmpl_string = data;
            },
            error: function(xhr,status,error) {
            	var errorMessage = getJSONValue(xhr.responseJSON, "error");
                var errorMessageCont = getJSONValue(xhr.responseJSON, "message");
                if(errorMessageCont){
                    openErrorBootBox(errorMessageCont);   
                }else if(errorMessage){
                    openErrorBootBox(errorMessage);
                }
            }
        });
        //Get the HTML from the template in the script tag and compile the template
        if(tmpl_string == "") return false;
        render.tmpl_cache[template] = Handlebars.compile(tmpl_string);
    }

    target.html(render.tmpl_cache[template](data));
    return true;
}

function openErrorBootBox(message){
	bootbox.alert({ 
	title: 'Error',
	message: message,
	buttons: {
	        ok: {
	            label: 'OK',
	            className: 'btn-info'
	        }
	    }
	});
}

function getJSONValue(data, id){
	var result = "";
	if(data){
		$.each(data, function(key,value){
			if(key == id){
				result = value;
			}
		});
	}

	return result;
}