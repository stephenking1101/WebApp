/**
Generic object for controllers
*/

var BaseController = function() {

};

BaseController.isDevelopment = (
  location.hostname === "localhost" ||
  location.hostname === "example.com"
);

BaseController.DataTypeEnum = {
    JSON : 0,
    TEXT : 1
};

/*
 * CRUD Operations
 */
BaseController.prototype.request = function(ajaxType, args) {

  //When invoked as a constructor, a new Object will be created, and this will be bound to that object, e.g. foo in var foo = new 
  var that = this;
 /* 利用deferred接口，使得任意操作都可以用done()和fail()指定回调函数

  这两个方法都用来绑定回调函数。done()指定非同步操作成功后的回调函数，fail()指定失败后的回调函数。

  代码如下:


  var deferred = $.Deferred();
  deferred.done(function(value) {
     alert(value);
  });*/
  var deferred = new $.Deferred();
  var JSONString = null;
  var logAPICalls = true;

  var defaultArgs = {
    log: BaseController.isDevelopment && logAPICalls,
    delay: 0,
    dataType: BaseController.DataTypeEnum.JSON
  };

  var defaultAttr = Object.keys(defaultArgs);
  for (var i=0; i<defaultAttr.length; i++) {
		if (!(defaultAttr[i] in args)) {
			args[defaultAttr[i]] = defaultArgs[defaultAttr[i]];
	    }
  }

  if (ajaxType == "POST" || ajaxType == "PUT") {
      JSONString = args.data ? JSON.stringify(args.data) : {};
  }

  if (ajaxType == "PUT" || ajaxType == "DELETE") {
      args.url = args.url.slice(-1) != "/" ? args.url + "/" : args.url;
  }

  if (args.params) {
	for (var key in args.params) {
      if (args.params.hasOwnProperty(key)) {
				args.url = args.url.replace(key, encodeURIComponent(args.params[key]));
	  }
	}
  }

  args.method = ajaxType;
  console.log("BaseController: " + args.url);
  setTimeout(function() {
      try {
    	  //The jqXHR objects returned by $.ajax() as of jQuery 1.5 implement the Promise
			$.ajax({
				type: ajaxType,
				url: args.url,
				data: JSONString,
				cache: false,
				beforeSend: function(xhr) {
					if (args.dataType == BaseController.DataTypeEnum.JSON) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
					}
                },
                headers:{'X-CustomToken':args.token ? args.token : ''},
                dataType: function(type) {
					if (type == BaseController.DataTypeEnum.JSON) {
						return "json";
					} else if (type == BaseController.DataTypeEnum.TEXT) {
						return "text";
					}
				}(args.dataType)
			})
			//allow children to define their owned callback for done() and fail()
			.done(args.done(that)(deferred, args))
			.fail(args.fail(that)(deferred, args));
		} catch (error) {
			console.log("BaseController: catch error");
			args.fail(that)(deferred, args)(error);
      }
  }, args.delay);

  //promise是deferred的只读版，可以通过promise对象，为原始的deferred对象添加回调函数，查询它的状态，但是无法改变它的状态，也就是说promise对象不允许你调用resolve和reject方法
  return deferred.promise();
};

//Create Call
BaseController.prototype.POST = function(args) {
 	return this.request("POST", args);
};

//Read Call
BaseController.prototype.GET = function(args) {
	return this.request("GET", args);
};

//Update Call
BaseController.prototype.PUT = function(args) {
	return this.request("PUT", args);
};

//Delete Call
BaseController.prototype.DELETE = function(args) {
	return this.request("DELETE", args);
};

/*
 * Response Handlers
 */

// Successful response with no modal
BaseController.prototype.responseDone = function() {
	var that = this;
	console.log("BaseController: that.responseDone() 1");
	return function(deferred, args) {
		console.log("BaseController: that.responseDone() 2");
		return function(data) {
			// By this point we're expecting a javascript object
			// hence, if this condition throws an exception
			// it implies we need to modify the server-side impl
			var obj = that.handleResponseObj(true, data, args);
			//一旦调用resolve()，就会依次执行done()和then()方法指定的回调函数；一旦调用reject()，就会依次执行fail()和then()方法指定的回调函数。
			deferred.resolve(obj);
			console.log("BaseController: that.responseDone() obj: " + obj);
		};
	};
};

//Successful response with modal
BaseController.prototype.responseDoneModal = function(successMessage, id) {
	var that = this;
	return function(deferred, args) {
		return function(data) {
			var obj = that.handleResponseObj(true, data, args);
			that.handleModal(obj, "Information", successMessage, id);
	        that.responseDone()(deferred, args)(data);
		};
	};
};

//Unsuccessful response with no modal
BaseController.prototype.responseFail = function() {
	var that = this;
	console.log("BaseController: that.responseFail() 1");
	return function(deferred, args) {
		console.log("BaseController: that.responseFail() 2");
		return function(jqXHR, textStatus, errorThrown) {
			// By this point we're expecting a javascript object
			// hence, if this condition throws an exception
			// it implies we need to modify the server-side impl
			var obj = that.handleResponseObj(false, jqXHR, args);
			//一旦调用resolve()，就会依次执行done()和then()方法指定的回调函数；一旦调用reject()，就会依次执行fail()和then()方法指定的回调函数。
			deferred.reject(obj);
			console.log("BaseController: that.responseFail() obj: " + obj);
		};
	};
};

//Unsuccessful response with modal
BaseController.prototype.responseFailModal = function(errorMessage, id) {
	var that = this;
	return function(deferred, args) {
		return function(jqXHR, textStatus, errorThrown) {
			var obj = that.handleResponseObj(false, jqXHR, args);
			that.handleModal(obj, "Error", errorMessage, id);
	        that.responseFail()(deferred, args)(jqXHR, textStatus, errorThrown);
		};
	};
};


/*
 * Handle XHR Object
 */

BaseController.prototype.handleResponseObj = function(successful, jqXHR, args) {

	var obj = jqXHR;

	if (obj && args.dataType == BaseController.DataTypeEnum.JSON) {
		if ("responseText" in obj) {
			if (obj.responseText) {
				try {
					obj = $.parseJSON(obj.responseText);
				} catch (error) {
					if(obj.responseText.indexOf("j_spring_security_check") > -1 &&
						 obj.responseText.indexOf("j_username") > -1 &&
			  	   obj.responseText.indexOf("j_password") > -1) {
						obj = {response: "Your session has expired, please login."};
					} else if (obj.responseText.indexOf("IBM WebSphere Application Server") > -1) {
						obj = {response: "The API appears to be unavailable."};
					} else {
						obj = {response: "Invalid server response, please check your request"};
					}
				}
			} else {
				obj = {response: ""};
			}
		}
		if ("response" in obj) {
			obj = obj.response;
		}
	}

    if (console && console.log && args.log) {
      args.response = obj;
      console.log("BaseController: " + "%c" + args.method + " %c" + args.url, successful ? "color:green;" : "color:red;", "font-weight:bold;");
      console.log(args);
    }

	return obj;

};


/*
 * Generic Modal Handler
 */

BaseController.prototype.handleModal = function(responseObj, title, message, id) {

	var validate = function(obj) {
		return(obj && obj !== "" && obj != "undefined");
	};

	if (validate(message)) {
		message += ": ";
	} else {
		message = "";
	}

	if (typeof responseObj == "string" && responseObj.toLowerCase() != "ok") { message += responseObj; }
	if (validate(message) && validate(id)) { message += " [" + id + "]"; }

	$('#progressModal').modal('hide');
	if (message && message !== "") {
		$('#messageModalHeader').html(title);
    	$('#messageModalBody').text(message);
    	$('#messageModal').modal('show');
	}

};

//if (console && console.log && BaseController.isDevelopment) {
//  console.log("%c                                                              ,---. \n ,----.   ,----.     ,------.              ,--.               |   | \n'  .-./   '.-.  |    |  .--. ' ,---.  ,---.|  |,-.  ,---.     |  .' \n|  | .---.  .' <     |  '--'.'| .-. || .--'|     / (  .-'     |  |  \n'  '--'  |/'-'  |    |  |\  \ ' '-' '\ `--.|  \  \ .-'  `)    `--'  \n `------' `----'     `--' '--' `---'  `---'`--'`--'`----'     .--.  \n                                                              '--'  \n", "color:red;");
//}
