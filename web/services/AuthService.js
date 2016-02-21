(function() {
	angular.module('taskAssistant').
	factory('AuthService', ['$http', '$cookies', '$rootScope', function($http, $cookies, $rootScope){
		var service = {}
		
		service.login = function(email, password, callbackFunction) {
			var response;
			//Make backend call for login validation
			/**
			$http.post('/api/v1/auth/validateuser', { params: { email: email, password: password} }).success(function(response) {
				callbackFunction(response);
			});
			*/
			//Mock backend response to be true for the moment
			response = { success : true };
			callbackFunction(response);
		};
		
		service.logout = function() {
			$rootScope.globals = {};
			$cookies.remove('globals');
			$http.defaults.headers.common.Authorization = 'Basic';
		};
		
		service.setCreds = function(email, password) {
			// TODO Replace with encryption
			var userCreds = email + ':' + password;
			
			$rootScope.globals = {
				currentUser: {
					email: email,
					userCreds: userCreds				
				}
			};
			
			$http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;)
			$cookies.put('globals', $rootScope.globals
		};
	}]);	
})();