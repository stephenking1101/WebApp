$(document).ready(function(){
	init_masonry();
	$.getJSON("data/product.json", {}, function(response,status,xhr) {
		alert("JSON Data: " + json.users[3].name);
	    console.log(json); 
	    var data = eval("(" +json + ")");
	    renderTemplate("products", json, "flash_templates");
	    console.log("why"); 
	    $.ajax({
	    	  url: "data/product.json",
	    	  data: {},
	    	  success: alert,
	    	  dataType: json
	    	});
	});
});

function init_masonry(){
	var $container = $('.item_container');
	$container.imagesLoaded( function(){
		$container.masonry({
			itemSelector : '.item',
			"gutter": 18,
			isFitWidth: true
		});
	});
}

var render = new Object();
function renderTemplate(template, data, target) {
	if ( !render.tmpl_cache ) { 
        render.tmpl_cache = {};
    }
    if ( !render.tmpl_cache[template] ) {
        var tmpl_url = window.location.protocol + "//" + window.location.host + '/template' + '/' + template + '.html';

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
	$.each(data, function(key,value){
		if(key == id){
			result = value;
		}
	});

	return result;
}