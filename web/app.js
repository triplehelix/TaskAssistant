(function() {
	var NO_AUTH_PAGES = ['/login', '/register'];
	
	angular.module('timeAssistant', ['ngRoute', 'ngCookies']).
		config(['$routeProvider', function($routeProvider) {
			$routeProvider.
				when('/', {
					templateUrl: 'views/home.html'
				}).
				when('/login', {
					templateUrl: 'views/login.html',
					controller: 'controllers/login_controller.js'
				}).
				when('/register', {
					templateUrl: 'views/register.html',
					controller: 'controllers/register_controller.js'
				}).
				when('/tasks', {
					templateUrl: 'views/tasks.html',
					controller: 'controllers/task_controller.js'
				}).
				when('/schedule', {
					templateUrl: 'views/schedule.html',
					controller: 'controllers/schedule_controller.js'
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