/**
 * Created by helix on 2016-03-20.
 */
(function () {
    angular.module('taskAssistant').controller('TaskModalController', ['$scope', '$log', '$uibModalInstance', function ($scope, $log, $uibModalInstance) {

        $log.info("TaskModalController Initialized.");
        $scope.dateOptions = {
            minDate: new Date(),
            showWeeks: true
        };
        
        $scope.submitTask = function () {
            $log.info("Submit Task called");
            var result = { success: "false", message: "System Error"};
            // TODO: Submit task

            $uibModalInstance.close(result);
        };

        $scope.cancelTask = function() {
            $log.info("Cancel adding Task");
            $uibModalInstance.dismiss('cancel');
        };

    }]);
})();