(function () {
	angular.module('taskAssistant').controller(
		'LoginController', ['$scope', '$location', '$log', 'AuthService', 'TaskService', function(
			$scope, $location, $log, AuthService, TaskService){
		var LoginController = this;
		$log.info("LoginController Initialized.");
		
		$scope.login = login;
		function login(){
			$log.info("$scope.login() called for email: " + $scope.LoginController.email);
			var that = this;
			that.email = $scope.LoginController.email;
			that.password = $scope.LoginController.password;
			//Step 1: Authenticate user
			AuthService.login($scope.LoginController.email, $scope.LoginController.password, function (response) {
				$scope.LoginController.fetchingData = true;
				$log.debug("logging in user with email: " + that.email);
				//Handle login response
				if (response.success){
					$log.debug("Authentication Successful... Updating cookies & continue to next step.");
					AuthService.setCreds($scope.LoginController.email, $scope.LoginController.password);
				}else{
					$log.warn("User Authentication failed");
					$scope.LoginController.error_message = "Authentication failed with inputted Credentials. Please validate and retry.";
					$scope.LoginController.fetchingData = false;
				}
			});

			if( ! $scope.LoginController.error_message ) {
				//Step 2: Collect that user's taskLists
				TaskService.getTasklists($scope.LoginController.email, function (response) {
					$scope.LoginController.fetchingData = true;
					$log.debug('Pulling TaskLists for user with email: ' + that.email);
					if (response.error) {
						$log.warn("Pulling TaskLists failed. Errror code=" + response.error.code);
						$scope.LoginController.error_message = "ERROR: " + response.error.message;
						$scope.LoginController.fetchingData = false
					} else {
						$log.info("Setting local tasks lists to: " + JSON.stringify(response.taskLists));
						TaskService.setLocalTaskLists(response.taskLists);
						$location.path('/');
					}
				});
			}else{
				$log.debug('Skipping getTasklist call because of Authentication failure');
			}
		}
		
	}]);
})();