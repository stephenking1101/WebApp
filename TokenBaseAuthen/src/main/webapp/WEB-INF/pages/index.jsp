<html>
<head>      
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>Maven + Spring MVC Web Project Example</h1>
 
<h3>Message : ${message}</h3>
<h3>Counter : ${counter}</h3>	

<a href="index.html">Enter the website</a>

<br>
<h1>API Test</h1>
<div>
<button type="button" onclick="getToken();">Get Token</button>
<div id="Token"></div>
            <div class="form-group">
              <button type="button" onclick="testToken();">Test Token</button> <br>
			  <label for="usr">Name:</label>
			  <input type="text" class="form-control" id="usr">
			</div>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootbox.min.js"></script>
<script src="js/BaseController.js"></script>
<script src="js/IndexController.js"></script>
<script>
var contextPath = "<%=request.getContextPath()%>";
var BASE_URL = window.location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '') + contextPath;
var indexController = new IndexController(BASE_URL);

function getToken(){
	console.log("getToken() called");
	$.ajax({
	    url: 'apitoken',
	    type: 'GET',
	    contentType: 'application/json; charset=utf-8',
	    dataType: 'json',
	    success: function(result,status,xhr){
	    	console.log("Success");
	    	console.log(result);
	    	console.log(status);
	    	console.log(xhr);
	    	$("#Token").text(getJSONValue(xhr.responseJSON, "token"));
	    },
	    error: function(xhr,status,error){
	    	console.log("Fail");
	    	console.log(xhr);
	    	console.log(status);
	    	console.log(error);
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
}

function testToken(){
	console.log("testToken() called");
    /* $.ajax({
        url: 'api/test',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {'X-CustomToken':$("#Token").text()},
        success: function(result,status,xhr){
        	console.log("Success");
            $("#usr").val(getJSONValue(xhr.responseJSON, "name"));
        },
        error: function(xhr,status,error){
        	console.log("Fail");
            var errorMessage = getJSONValue(xhr.responseJSON, "error");
            var errorMessageCont = getJSONValue(xhr.responseJSON, "message");
            if(errorMessageCont){
                openErrorBootBox(errorMessageCont);   
            }else if(errorMessage){
                openErrorBootBox(errorMessage);
            }
        },
        async:false
    }); */
	indexController.getAll($("#Token").text())
	   .done(function(xhr){
		        //the xhr is passed by deferred.resolve(obj) in BaseController
		        console.log(xhr);
		        $("#usr").val(getJSONValue(xhr, "name"));
		     });
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
</script>
<!-- This is used for messages displayed to the user with just one button to dismiss the modal -->
<div id="messageModal" class="modal fade">
    <div class="modal-dialog">
            <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3 id="messageModalHeader">Error</h3>
    </div>
    <div id="messageModalBody" class="modal-body">
        <p>Please try again or contact support.</p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn btn-danger" data-dismiss="modal"
            aria-hidden="true">Ok</a>
    </div>
            </div>
    </div>
</div>

<!-- This is used for progress -->
<div class="modal fade" id="progressModal" tabindex="-1" role="dialog"
    aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
            <div class="modal-content">
    <div class="modal-header">
        <h3 id="progressModalHeader"></h3>
    </div>
    <div class="modal-body">
        <div class="progress progress-danger progress-striped active">
            <div class="bar" style='width: 100%'></div>
        </div>
    </div>
            </div>
    </div>
</div>
</body>
</html>
