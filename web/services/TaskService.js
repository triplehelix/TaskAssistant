(function() {
	angular.module('taskAssistant').
	factory('TaskService', ['$http', '$cookies', '$rootScope', function($http, $cookies, $rootScope){
		var service = {};
		var mock = true;

		service.getTasklists = function(email, callbackFunction) {
			if (mock == false) {
				// Make backend call for tasklists for user
				$http.get('/api/v1/user/gettasklists', {params: {userid: email}}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response = {};
                if (email == "mike@test.com") {
                    response = [{id: 1, name: "Mikes Tasks", description: "A list of Mikes tasks"}];
                } else {
                    response = {
                        error: {
                            code: 104,
                            message: "A Task list does not exist for a user with that email."
                        }
                    };
                }
				callbackFunction(response)
			}
		};

		service.setTaskLists = function (taskLists) {
			$rootScope.globals.currentUser.taskLists = taskLists;
			$cookies.put('globals', $rootScope.globals);
		};

		service.addTasklist = function(email, taskList, callbackFunction) {
			if (mock == false) {
				// Make backend call to add tasklist for user
				$http.post('/api/v1/user/addtasklist', {
					params: {
						userid: email, name: taskList.name,
						description: taskList.description, permission: owner
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response = {};
                if (taskList.id != 1) {
                    response = {success: true};
                } else {
                    response = {
                        error: {
                            code: 103,
                            message: "A Task list already exists with that id."
                        }
                    };
                }
				callbackFunction(response)
			}
		};

		service.updateTasklist = function(taskList, callbackFunction) {
			if (mock == false) {
				// Make backend call to add tasklist for user
				$http.post('/api/v1/user/updatetasklist', {
					params: {
						tasklistid: taskList.id, name: taskList.name,
						description: taskList.description, permission: owner
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
                var response = {};
                if (taskList.id == 1) {
                    response = {success: true};
                } else {
                    response = {
                        error: {
                            code: 102,
                            message: "A Task list does not exist with that id."
                        }
                    };
                }
				callbackFunction(response)
			}
		};
		
		service.getTasks = function(taskListId, filter, callbackFunction) {
			if (mock == false) {
				// Make backend call to get tasks in given tasklist
				$http.get('/api/v1/tasklist/gettasks', {
					params: {
						tasklistid: taskListId,
						filter: null
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response;
				//Mock backend response for the moment
				response = [{
					id: 1, name: "Task Assistant Application", estimated_time: 40, invested_time: 10,
					important: 0, urgent: 0, deadline_date: null, status: "IN_PROGRESS"
				},
					{
						id: 2, name: "Shopping list application", estimated_time: 40, invested_time: 10,
						important: 0, urgent: 0, deadline_date: null, status: "IN_PROGRESS"
					},
					{
						id: 3, name: "Serial Number Manager application", estimated_time: 40, invested_time: 10,
						important: 0, urgent: 0, deadline_date: null, status: "NEW"
					}];

				callbackFunction(response);
			}
		};

		service.addTask = function(taskListId, task, callbackFunction) {
			if (mock == false) {
				// Make backend call to add task to tasklist
				$http.post('/api/v1/task/addtask', {
					params: {
						tasklistid: taskListId, categoryid: task.categoryid,
						name: task.name, estimated_time: task.estimated_time, invested_time: task.invested_time,
						scheduled_time: task.scheduled_time, importance: task.importance, urgency: task.urgency,
						deadline: task.deadline, status: task.status
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response = {};
                if (taskListId == 1) {
                    response = {success: true};
                } else {
                    response = {
                        error: {
                            code: 106,
                            message: "Failed to add task because a Task list does not exist with that id."
                        }
                    };
                }
				callbackFunction(response)
			}
		};

		service.updateTask = function(task, callbackFunction) {
			if (mock == false) {
				// Make backend call to add task to tasklist
				$http.post('/api/v1/task/updatetask', {
					params: {
						taskid: task.id, categoryid: task.categoryid,
						name: task.name, estimated_time: task.estimated_time, invested_time: task.invested_time,
						scheduled_time: task.scheduled_time, importance: task.importance, urgency: task.urgency,
						deadline: task.deadline, status: task.status
					}
				}).success(function (response) {
					callbackFunction(response);
				});
			} else {
				var response = {};
                if (task.id == 1) {
                    response = {success: true};
                } else {
                    response = {
                        error: {
                            code: 107,
                            message: "Failed to update task because a Task does not exist with that id."
                        }
                    };
                }
				callbackFunction(response)
			}
		};
		
		return service;
	}]);	
})();