/**
 * Created by helix on 2016-02-27.
 */
(function(){
    angular.module('taskAssistant').controller('NavController', ['$scope', '$location', '$log', function($scope, $location, $log){
       $scope.isActive = function (viewLocation) {
           $log.debug("Current Path = " + $location.path());
            return viewLocation == $location.path();
       };
    }]);
})();