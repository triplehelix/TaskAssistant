/**
 * Created by helix on 2016-03-20.
 */
(function () {
    angular.module('taskAssistant').controller('TaskModalController', ['$scope', '$log', '$uibModalInstance', function ($scope, $log, $uibModalInstance, TaskService) {

        $log.info("TaskModalController Initialized.");
        $scope.dateOptions = {
            minDate: new Date(),
            showWeeks: true
        };
        
        $scope.submitTask = function () {
            $log.info("Submit Task called.");
            var task = null;
            task.name = $scope.TaskModal.name;
            task.important = $scope.TaskModal.important;
            task.urgent = $scope.TaskModal.urgent;
            task.estimatedTime = $scope.TaskModal.estimatedTime;
            task.investedTime = $scope.TaskModal.investedTime;
            task.status = $scope.TaskModal.status;
            task.deadline = $scope.TaskModal.deadline;
            task.notes = $scope.TaskModal.notes;

            var result = null;
            TaskService.addTask(1, task, function(response){
                $scope.TaskModal.fetchingData = true;
                if (response.success){
                    $log.debug("Task successfully added.");
                    //TODO Delayed Redirect
                    result = { success: "true", message: "Task Successfully added."};
                }else{
                    $log.debug("Adding Task Failed.")
                    // TODO Display Error and Release Form
                    result = { success: "false", message: "System Error"};
                    $scope.TaskModal.fetchingData = false;
                }
            });

            $uibModalInstance.close(result);
        };

        $scope.cancelTask = function() {
            $log.info("Cancel adding Task");
            $uibModalInstance.dismiss('cancel');
        };

    }]);
})();