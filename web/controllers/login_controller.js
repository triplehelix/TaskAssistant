(function () {
	angular.module('taskAssistant').controller('LoginController', ['$scope', '$location', '$log', 'AuthService', function($scope, $location, $log, AuthService){
		var LoginController = this;
		$log.info("LoginController Initialized.");
		
		$scope.login = login;
		function login(){
			$log.info("$scope.login() called for email: " + $scope.LoginController.email);
			var that = this;
			that.email = $scope.LoginController.email;
			that.password = $scope.LoginController.password;
			AuthService.login($scope.LoginController.email, $scope.LoginController.password, function (response) {
				$scope.LoginController.fetchingData = true;
				$log.debug("logging in user with email: " + that.email);
				//Handle login response
				if (response.success){
					$log.debug("Authentication Successful... Updating cookies & redirecting to home page.");
					AuthService.setCreds($scope.LoginController.email, $scope.LoginController.password);
					$location.path('/');
				}else{
					$log.warn("User Authentication failed");
					$scope.LoginController.error_message = "Authentication failed with inputted Credentials. Please validate and retry."
					$scope.LoginController.fetchingData = false;
				}
			});
			
		};
		
	}]);
})();