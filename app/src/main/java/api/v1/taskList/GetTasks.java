package api.v1.taskList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.model.Task;
import api.v1.repo.TaskRepository;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.ArrayList;

import api.v1.model.TaskList;

/**
 * This api is used to retrieve a given taskList. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * taskList.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/GetTaskList")
public class GetTasks extends TaskRequestHandler {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, 
                HttpServletResponse response)throws ServletException, IOException {
        boolean error = false;
        String errorMsg = "no error";
        String listOfTasksAsJson="";
        TaskList taskList = new TaskList();
        ArrayList<Task> listOfTasks;
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            taskList.setId(Integer.parseInt((String)jsonRequest.get("id")));
            taskList=taskListRepository.get(taskList);
            //TaskRepository takes a valid TaskList and returns an ArrayList of corresponding tasks.
            listOfTasks=taskRepository.getListOfTasks(taskList);
            /*
             * List<String> foo = new ArrayList<String>();
             * foo.add("A");
             * foo.add("B");
             * foo.add("C");
             *
             * String json = new Gson().toJson(foo );
            */
            listOfTasksAsJson=new Gson().toJson(listOfTasks);

        } catch (BusinessException b) {
            log.error("An error occurred while handling an GetTaskList  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an GetTaskList Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("listOfTaskIdsAsJson", listOfTasksAsJson);
        }
        sendMessage(jsonResponse, response);
    }
}
