(function () {
	angular.module('taskAssistant').controller('DashboardController', ['$scope', '$log', '$uibModal', function ($scope, $log, $uibModal) {
		// Open task Modal
		$scope.openAddTaskModal = function (){
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'views/modals/task_add.html',
				controller: 'TaskModalController',
				resolve: {}
			});

			modalInstance.result.then(function (result){
				// TODO: Result?
				if(!result.success){
					$log.error("Failed to submit task because of: " + result.message);
				}else{
					$log.log("Task submitted successfully");
				}
			}, function () {
				$log.info("Task Modal closed with no submission.");
			});
		};
			
	}]);
})();