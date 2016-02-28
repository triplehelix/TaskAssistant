(function () {
	angular.module('taskAssistant').controller('LogoutController', ['$scope', '$log', '$rootScope', 'AuthService', function($scope, $log, $rootScope, AuthService){
		$log.debug("LogoutController Initialized.");
		var LogoutController = this;
		$scope.currentUser = ($rootScope.globals.currentUser) ? $rootScope.globals.currentUser.email : null;
		
		$scope.logout = logout;
		function logout(){
			$log.info("logout() called.");
			AuthService.logout();
			$scope.currentUser = ($rootScope.globals.currentUser) ? $rootScope.globals.currentUser.email : null;
		};
	}]);
})();