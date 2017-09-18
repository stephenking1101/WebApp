IndexController = function (aBASE_URL){
	this._BASE_URL = aBASE_URL;
	this._testLocations = this._BASE_URL + "/api/test";
};

IndexController.prototype = Object.create(BaseController.prototype);

IndexController.prototype.getAll = function(token) {
	console.log("IndexController: " + this._testLocations);
	return this.GET({
 		url: this._testLocations,
 		token: token,
 		//dataType: BaseController.DataTypeEnum.JSON,
 		done: function(handle) {
 			console.log("IndexController: getAll done");
 			return handle.responseDone();
 		},
 		fail: function(handle) {
 			console.log("IndexController: getAll fail");
 			return handle.responseFailModal("Unable to fetch all");
 			//return handle.responseFail();
 		}
 	});
};
