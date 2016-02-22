(function() {
	angular.module('taskAssistant').
	factory('UserService', ['$http', '$cookies', '$rootScope', function($http, $cookies, $rootScope){
		var service = {}
		
		service.createUser = function(email, password, callbackFunction) {
			var response;
			//Make backend call for creation of user
			/**
			$http.post('/api/v1/auth/createuser', { params: { email: email, password: password} }).success(function(response) {
				callbackFunction(response);
			});
			*/
			//Mock backend response to be true for the moment
			if(email){
				response = { success : true };
			}else{
				response = { error : { code: 101, message: "invalid email"} };
			}
			callbackFunction(response);
		};
		
		service.changePassword  = function(email, password, callbackFunction) {
			var response;
			//Make backend call for updating of password
			/**
			$http.post('/api/v1/auth/updatePassword', { params: { email: email, password: password} }).success(function(response) {
				callbackFunction(response);
			});
			*/
			//Mock backend response to be true for the moment
			if(email == "mike@test.com"){
				response = { success : true };
			}else{
				response = { error : { code: 101, message: "invalid email"} };
			}
			callbackFunction(response);
		};
		
		service.forgotPassword = function(email, password, callbackFunction) {
			var response;
			//Make backend call for forgetting password
			/**
			$http.post('/api/v1/auth/forgotPassword', { params: { email: email, password: password} }).success(function(response) {
				callbackFunction(response);
			});
			*/
			//Mock backend response to be true for the moment
			if(email == "mike@test.com"){
				response = { success : true };
			}else{
				response = { error : { code: 101, message: "invalid email"} };
			}
			callbackFunction(response);
		};
		
		return service;
	}]);	
})();