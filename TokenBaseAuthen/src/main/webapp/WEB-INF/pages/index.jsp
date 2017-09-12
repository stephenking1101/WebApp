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
			  <label for="usr">Token:</label>
			  <input type="text" class="form-control" id="usr">
			  <button type="button" onclick="testToken();">Test Token</button>
			</div>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootbox.min.js"></script>
<script>
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
    $.ajax({
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
</body>
</html>
