package api.v1.taskList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import api.v1.model.TaskList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
/**
 * This api is used to retrieve a given taskList. Use the class member
 * doGet(HttpServletRequest, HttpServletResponse) to retrieve this
 * taskList.
 *
 *  @author Ken Lyon
 */
@WebServlet("/api/v1/taskList/GetTaskList")
public class GetTaskList extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskList.class);
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
        TaskList taskList = new TaskList();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            taskList.setId(Integer.parseInt((String)jsonRequest.get("id")));
            //LOGGER.debug("Here is the purported TaskList id: " + taskList.getId());
            //LOGGER.debug("Here is the purported TaskList name: " + taskList.getName());
            //LOGGER.debug("Here is the purported TaskList description: " + taskList.getDescription());
            taskList=taskListRepository.get(taskList);
            //LOGGER.debug("Here is the internal TaskList id: " + taskList.getId());
            //LOGGER.debug("Here is the internal TaskList name: " + taskList.getName());
            //LOGGER.debug("Here is the internal TaskList description: " + taskList.getDescription());

            /**
             * TODO: Return an instance of this taskList.
             * Okay, so do we need to send a success response AND the taskList? How
             * does the print stream interpret this? I have no fucking idea!
             *
             */
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an GetTaskList  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an GetTaskList Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("TaskList", taskList.toJson());
        }
        sendMessage(jsonResponse, response);
    }
}
