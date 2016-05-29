(function() {
	angular.module('taskAssistant').
	factory('AuthService', ['$http', '$cookies', '$rootScope', function($http, $cookies, $rootScope){
		var service = {};
		var mock = true;
		
		service.login = function(email, password, callbackFunction) {
			if (mock == false) {
				//Make backend call for login validation
				$http.post('/api/v1/auth/validateuser', {
					params: {
						email: email,
						password: password
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response;
				//Mock backend response to be true for the moment
				if (email == "mike@test.com") {
					response = {success: true};
				} else {
					response = {error: {code: 101, message: "invalid email"}};
				}
				callbackFunction(response);
			}
		};
		
		service.logout = function() {
			$rootScope.globals = {};
			$cookies.remove('globals');
			$http.defaults.headers.common.Authorization = 'Basic';
		};
		
		service.setCreds = function(email, password) {
			var userCreds = encrypt(email,password);
			$rootScope.globals = {
				currentUser: {
					email: email,
					userCreds: userCreds				
				}
			};
			$http.defaults.headers.common['Authorization'] = 'Basic ' + userCreds;
			$cookies.put('globals', $rootScope.globals);
		};

		encrypt = function(email, password){
			// TODO implement encryption algorithm
			var encryptedToken = email + ':' + password;
			return encryptedToken;
		};
		
		return service;
	}]);	
})();