(function () {
    angular.module('taskAssistant').controller(
        'TaskModalController', ['$scope', '$log', '$uibModalInstance', 'TaskService', '$rootScope', function (
            $scope, $log, $uibModalInstance, TaskService, $rootScope) {

        $log.info("TaskModalController Initialized.");
        $scope.dateOptions = {
            minDate: new Date(),
            showWeeks: true
        };

        $scope.taskLists = $rootScope.globals.currentUser.taskLists;
        
        $scope.submitTask = function () {
            $log.info("Submit Task called.");
            var task = {};
            task.name = $scope.name;
            task.important = $scope.important;
            task.urgent = $scope.urgent;
            task.estimatedTime = $scope.estimatedTime;
            task.investedTime = $scope.investedTime;
            task.status = $scope.status;
            task.deadline = $scope.deadline;
            task.notes = $scope.notes;
            task.tasklistid = $scope.taskListId;

            var result = null;
            TaskService.addTask(task.tasklistid, task, function(response){
                $log.debug("TaskService.addTask called.");
                $scope.fetchingData = true;
                if (response.success){
                    $log.debug("Task successfully added.");
                    //TODO Delayed Redirect
                    result = { success: "true", message: "Task Successfully added."};
                    $uibModalInstance.close(result);
                }else{
                    $log.debug("Adding Task Failed: " + response.error.message);
                    // TODO Display Error and Release Form
                    result = { success: "false", message: "System Error"};
                    $scope.fetchingData = false;
                }
            });
        };

        $scope.cancelTask = function() {
            $log.info("Cancel adding Task");
            $uibModalInstance.dismiss('cancel');
        };

    }]);
})();