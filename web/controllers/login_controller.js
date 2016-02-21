(function () {
	angular.module('timeAssistant', []).controller('LoginController', ['$location', '$log', function($location, $log){
		var that = this;
		var login = function(){
			$log.info("logging in email: " + that.email);
			AuthService.login(that.email, that.password, function (response) {
				that.fetchingData = true;
				//Handle login response
				if (response.success){
					AuthService.SetCredentials(that.email, that.password);
					$location.path('/');
				}else{
					ErrorService.error(response.message);
					that.fetchingData = false;
				}
			});
			
		};
	}]);
})();