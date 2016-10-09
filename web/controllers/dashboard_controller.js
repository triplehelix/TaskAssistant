(function () {
	angular.module('taskAssistant').controller('DashboardController', ['$scope', '$log', '$uibModal', 'TaskService',
		'$cookies', '$rootScope', function ($scope, $log, $uibModal, TaskService) {

		// Load top tasks
		var taskListId = null;
		var setTaskListId = function (p_taskListId) {
			if (null != taskListId) {
				taskListId = p_taskListId;
			}else{
				taskListId = TaskService.getLocalTaskLists();
			}
		};
		$scope.userTopTasks = [];
		var topTasks = function () {
			$log.info("topTasks Function called.");
			TaskService.getTasks(taskListId, null, function(response){
				if ( ! response.error ) {
					// TODO logic
					$scope.userTopTasks = response.listOfTaskIdsAsJson;
				}else{
					// Handle error
					$log.error("Error in topTasks function.");
				}
			});
		};

		// Open task Modal
		$scope.openAddTaskModal = function (){
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'views/modals/task_add.html',
				controller: 'TaskModalController',
				resolve: {}
			});

			modalInstance.result.then(function (result){
				if(!result.success){
					$log.error("Failed to submit task because of: " + result.message);
				}else{
					$log.log("Task submitted successfully");
					topTasks();
				}
			}, function () {
				$log.info("Task Modal closed with no submission.");
			});
		};

		// Init function to load on page load
		var init = function () {
			setTaskListId(null);
			topTasks();
		};

		init();
	}]);
})();