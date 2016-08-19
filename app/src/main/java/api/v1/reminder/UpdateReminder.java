package api.v1.reminder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import api.v1.TaskRequestHandler;
import api.v1.error.CriticalException;
import api.v1.model.Task;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.simple.JSONObject;
import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.error.Error;
import api.v1.helper.ErrorHelper;
import java.io.IOException;
import java.util.Date;
import api.v1.model.Reminder;

/**
 * This api is used to update a given reminder. Use the class member
 * doPut(HttpServletRequest, HttpServletResponse) to update this
 * reminder.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/reminder/UpdateReminder")
public class UpdateReminder extends TaskRequestHandler {
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
        Reminder clientReminder = new Reminder();
        Reminder serverReminder = new Reminder();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest = parseRequest(request.getParameter("params"));
            //Create and validate the client reminder.
            log.debug("Create and validate the client reminder.");
            Date reminderDate = parseJsonDateAsDate((String)jsonRequest.get("reminderTime"));
            Integer taskId =  parseJsonIntAsInt((String)jsonRequest.get("taskId"));
            Integer reminderId =  parseJsonIntAsInt((String)jsonRequest.get("id"));
            clientReminder.setId(reminderId);
            clientReminder.setTaskId(taskId);
            clientReminder.setReminderTime(reminderDate);
            verifyTaskExists(clientReminder.getTaskId());
            log.debug("So, now we have the client reminder id, " + clientReminder.getId() + " and the task id it points to " + clientReminder.getTaskId() + ".");

            // Clean references to the current reminder using the serverReminder:
            serverReminder=reminderRepository.get(clientReminder);
            log.debug("Now, we can fetch the server reminder and clean it's reference to the task. Which, by the way is " + serverReminder.getTaskId() + ".");

            removeReferences(serverReminder);
            addReminderToTask(clientReminder);
            reminderRepository.update(clientReminder);

        } catch (BusinessException b) {
            log.error("An error occurred while handling an PutReminder  Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        } catch (SystemException s) {
            log.error("An error occurred while handling an PutReminder Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        } catch (CriticalException c) {
            log.error("An error occurred while handling an PutReminder Request: {}.", jsonRequest.toJSONString(), c);
            errorMsg = "Error. " + c.getMessage();
            errorCode = c.getError().getCode();
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

    private void removeReferences(Reminder reminder) throws BusinessException, SystemException, CriticalException{
        log.debug("So we have made it to 'removeReferences' and here is the reminder id and it's task id respectfully: " + reminder.getId() + ", " + reminder.getTaskId() + ".");

        Task task=new Task();
        task.setId(reminder.getTaskId());
        task=taskRepository.get(task);
        log.debug("And just so we are clear, this is the Reminder as a JSON: " + reminder.toJson());
        log.debug("And here is the Task as a JSON: " + task.toJson());
        log.debug("When we fetch the task, we find it's id and reminder id(s) are: " + task.getId() + ", " + new Gson().toJson(task.getReminderIds()) + ".");
        log.debug("So, now we ask the question: does the Reminder array belonging to the task " + task.getId() + " Contain the Reminder id " + reminder.getId() + "?");
        if(task.getReminderIds().contains(reminder.getId())){
            log.debug("Yes. The answer is yes.");
            task.getReminderIds().remove((Object)reminder.getId());
        }
        else {
            log.debug("Nope. I guess not.");
            throw new CriticalException("Critical error! Cannot clean this Category. Task {name="
                    + task.getName() + ", id=" + task.getId()
                    + "} does not reference this object!", Error.valueOf("API_DELETE_OBJECT_FAILURE"));
        }
    }
}
