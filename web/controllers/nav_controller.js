/**
 * Created by helix on 2016-02-27.
 */
(function(){
    angular.module('taskAssistant').controller('NavController', ['$scope', '$location', '$log', '$rootScope', function($scope, $location, $log, $rootScope){
        $rootScope.$on('$locationChangeStart', function(event, next, current) {
            $scope.currentUser = ($rootScope.globals.currentUser) ? $rootScope.globals.currentUser.email : null;
        });
        $scope.isActive = function (viewLocation) {
            return viewLocation == $location.path();
       };
    }]);
})();