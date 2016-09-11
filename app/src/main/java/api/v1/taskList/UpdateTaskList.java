package api.v1.taskList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskListRequestHandler;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.TaskList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
/**
 * This api is used to update a given taskList. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * taskList.
 *
 * TODO currently this api does not update Tasks that have been added or deleted on the client side.
 * @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/UpdateTaskList")
public class UpdateTaskList extends TaskListRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTaskList.class);
    /**
     * Update a TaskList object. Include references to all Tasks that you
     * want to keep since dereferenced Tasks will be deleted.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request,
                HttpServletResponse response)throws ServletException, IOException {
        boolean error = false;
        String errorMsg = "no error";
        TaskList taskList = new TaskList();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            taskList.setId(Integer.parseInt((String)jsonRequest.get("id")));
            taskList=taskListRepository.get(taskList);
            taskList.setName((String)jsonRequest.get("name"));
            taskList.setDescription((String)jsonRequest.get("description"));
            taskListRepository.update(taskList);

        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an PutTaskList  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an PutTaskList Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
        }
        sendMessage(jsonResponse, response);
    }
}
