(function() {
	//var NO_AUTH_PAGES = ['/login', '/register'];
	
	angular.module('taskAssistant', ['ngRoute', 'ngCookies' ]).
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
		}]);
		/**.run(['$rootScope', '$location', '$cookies', '$http', '$log', function($rootScope, $location, $cookies, $http, $log) {
			$rootScope.globals = $cookies.get('globals') || {};
		    $log.info("globals: " + $rootScope.globals);
			if ($rootScope.globals.currentUser) {
				$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
			}
			
			$rootScope.$on('$locationChangeStart', function(event, next, current){
				var pageRestricted = $.inArray($location.path(), NO_AUTH_PAGES) === -1;
				
				var authenticated = $rootScope.globals.currentUser;
				if(pageRestricted && !authenticated){
					$location.path('/login');
				}
			});
		}]); */

})();