(function () {
	angular.module('taskAssistant').
    controller('RegisterController', ['$scope', '$log', '$timeout', '$location', 'UserService', 'AuthService', function ($scope, $log, $timeout, $location, UserService, AuthService) {
		var RegisterController = this;
        $log.info('RegisterController initialized');
		$scope.register = register;

		function register() {
            $log.info("$scope.register() called for email: " + $scope.RegisterController.email);
            var that = this;
            that.email = $scope.RegisterController.email;
            that.password = $scope.RegisterController.password;
            that.checkPassword = $scope.RegisterController.checkPassword;
            if (that.password != that.checkPassword) {
                $log.error("The submitted passwords do not match. Aborting Registration.");
                var msg = "Inputted passwords do not match. Please re-enter and try again.";
                $scope.RegisterController.error_message = msg;
            } else {
                UserService.createUser(that.email, that.password, function (response) {
                    //Handle response
                    $scope.RegisterController.fetchingData = true;
                    $log.debug("Attempting to Register user with email: " + that.email);
                    if (response.success) {
                        //User added
                        $log.debug("User registered with email: " + that.email);
                        $scope.RegisterController.success_message = "User Successfully created! Logging in user for email: " + that.email;
                        AuthService.setCreds(that.email, that.password);
                        $log.debug("Automatically logged in user with e-mail: " + that.email);
                        $scope.RegisterController.fetchingData = false;
                        $timeout(function () {
                            $location.path('/');
                        }, 5000)
                    } else {
                        //User not added
                        $log.error("User registration failed for email: " + that.email + " with error message: " + response.error.message);
                        $scope.RegisterController.error_message = "User Registration failed with the following error: " + response.error.message;
                        $scope.RegisterController.fetchingData = false;
                    }
                });
            }
        };
	}]);
})();