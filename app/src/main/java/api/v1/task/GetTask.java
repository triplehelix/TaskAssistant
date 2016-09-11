package api.v1.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.TaskRequestHandler;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import api.v1.model.Task;

/**
 * This api is used to fetch an existing task. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a new
 * task.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/task/GetTask")
public class GetTask extends TaskRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTask.class);
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

        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        Task task = new Task();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            task.setId(parseJsonIntAsInt((String)jsonRequest.get("id")));
            task=taskRepository.get(task);
        } catch (BusinessException b) {
            LOGGER.error("An error occurred while handling an GetTask Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            LOGGER.error("An error occurred while handling an GetTask Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error) {
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        } else {
            jsonResponse.put("success", true);
            jsonResponse.put("task", task.toJson());
        }
        sendMessage(jsonResponse, response);
    }
}
