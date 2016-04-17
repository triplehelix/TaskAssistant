(function() {
	var NO_AUTH_PAGES = ['/login', '/register'];
	
	angular.module('taskAssistant', ['ngRoute', 'ngCookies', 'ngAnimate', 'ui.bootstrap']).
		config(['$routeProvider', function($routeProvider) {
			$routeProvider.
				when('/', {
					templateUrl: 'views/dashboard.html',
					controller: 'DashboardController'
				}).
				when('/login', {
					templateUrl: 'views/login.html',
					controller: 'LoginController'
				}).
				when('/logout', {
					templateUrl: 'views/logout.html',
					controller: 'LogoutController'
				}).
				when('/register', {
					templateUrl: 'views/register.html',
					controller: 'RegisterController'
				}).
				when('/tasks', {
					templateUrl: 'views/tasks.html',
					controller: 'TaskController'
				}).
				when('/schedule', {
					templateUrl: 'views/schedule.html',
					controller: 'ScheduleController'
				}).
				when('/reports', {
					templateUrl: 'views/reports.html',
					controller: 'ReportsController'
				}).
				otherwise({redirectTo: '/login'});
		}]).run(['$rootScope', '$location', '$cookies', '$http', '$log', function($rootScope, $location, $cookies, $http, $log) {
			$rootScope.globals = $cookies.get('globals') || {};
		    $log.debug("globals.currentUser: " + $rootScope.globals.currentUser);
			if ($rootScope.globals.currentUser) {
				$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
			}
			
			$rootScope.$on('$locationChangeStart', function(event, next, current){
				var pageRestricted = NO_AUTH_PAGES.indexOf($location.path()) == -1;
				var authenticated = $rootScope.globals.currentUser;
				$log.debug("Location change event-pageRestricted:" + pageRestricted + " authenticated:" + !!authenticated);
				if(pageRestricted && !authenticated){
					$location.path('/login');
				}else if(!pageRestricted && authenticated){
					$location.path('/logout');
				}
			});
		}]);

})();