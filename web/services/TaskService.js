(function() {
	angular.module('taskAssistant').
	factory('TaskService', ['$http',  function($http){
		var service = {};

		service.getTasklists = function(email, callbackFunction) {
			var response;
			// Make backend call for tasklists for user
			$http.get('/api/v1/user/gettasklists', { params: { userid: email } }).success(function(response) {
				callbackFunction(response);
			});
		};

		service.addTasklist = function(email, tasklist, callbackFunction) {
			var response;
			// Make backend call to add tasklist for user
			$http.post('/api/v1/user/addtasklist', { params: { userid: email, name: tasklist.name,
				description: tasklist.description, permission: owner } }).success(function(response) {
				callbackFunction(response);
			});
		};

		service.updateTasklist = function(tasklist, callbackFunction) {
			var response;
			// Make backend call to add tasklist for user
			$http.post('/api/v1/user/updatetasklist', { params: { tasklistid: tasklist.id, name: tasklist.name,
				description: tasklist.description, permission: owner } }).success(function(response) {
				callbackFunction(response);
			});
		};
		
		service.getTasks = function(tasklist, filter, callbackFunction) {
			var response;
			// Make backend call to get tasks in given tasklist
/*			$http.get('/api/v1/tasklist/gettasks', { params: { tasklistid: tasklist.id, filter: null } }).success(function(response) {
				callbackFunction(response);
			});*/
			//Mock backend response for the moment
			response = [ {id: 1, name: "Task Assistant Application", estimated_time: 40, invested_time: 10,
				important: 0, urgent: 0, deadline_date: null, status: "WIP"}, ];

			callbackFunction(response);
		};

		service.addTask = function(tasklist, task, callbackFunction) {
			var response;
			// Make backend call to add task to tasklist
			$http.post('/api/v1/task/addtask', { params: { tasklistid: tasklist.id, categoryid: task.categoryid,
				name: task.name, estimated_time: task.estimated_time, invested_time: task.invested_time,
				scheduled_time: task.scheduled_time, importance: task.importance, urgency: task.urgency,
				deadline: task.deadline, status: task.status } }).success(function(response) {
				callbackFunction(response);
			});
		};

		service.updateTask = function(task, callbackFunction) {
			var response;
			// Make backend call to add task to tasklist
			$http.post('/api/v1/task/updatetask', { params: { taskid: task.id, categoryid: task.categoryid,
				name: task.name, estimated_time: task.estimated_time, invested_time: task.invested_time,
				scheduled_time: task.scheduled_time, importance: task.importance, urgency: task.urgency,
				deadline: task.deadline, status: task.status } }).success(function(response) {
				callbackFunction(response);
			});
		};
		
		return service;
	}]);	
})();