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
 * doPost(HttpServletRequest, HttpServletResponse) to update this
 * taskList.
 *
 * TODO currently this api does not update Tasks that have been added or deleted on the client side.
 * @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/UpdateTaskList")
public class UpdateTaskList extends TaskListRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTaskList.class);
    /**
     * Presently, this class only updates the Name and Description of a TaskList. It
     * cannot be used to delete or add Tasks.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request,
                HttpServletResponse response)throws ServletException, IOException {
        boolean error = false;
        String errorMsg = "no error";
        TaskList clientTaskList = new TaskList();
        TaskList serverTaskList;
        int errorCode = 0;
        String json="";
        try {
            json=request.getParameter("params");
            clientTaskList=(TaskList)getMyObject(json, clientTaskList);
            serverTaskList=taskListRepository.get(clientTaskList);
            serverTaskList.setName(clientTaskList.getName());
            serverTaskList.setDescription(clientTaskList.getDescription());
            taskListRepository.update(serverTaskList);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an PutTaskList  Request: {}.", json, b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an PutTaskList Request: {}.", json, s);
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
