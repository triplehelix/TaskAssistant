package api.v1.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.v1.error.BusinessException;
import api.v1.error.SystemException;
import api.v1.helper.ErrorHelper;
import org.json.simple.JSONObject;

import api.v1.AuthRequestHandler;
import api.v1.model.User;

/**
 * This api is used to create a new User. Use the class member
 * doPost(HttpServletRequest, HttpServletResponse) to create a
 * new User. A JSONObject must provide a string email and a
 * string password.
 *
 * @author Ken Lyon
 */
@WebServlet("/api/v1/auth/CreateUser")
public class CreateUser extends AuthRequestHandler{
    /**
     * POST
     * request 
     *   email
     *   password
     * response
     *   success
     *   error
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request,
					   HttpServletResponse response)throws ServletException, IOException {
        //First get the email and password.
        boolean error=false;
        String errorMsg = "no error";
        User user=new User();
        int errorCode = 0;
        JSONObject jsonRequest = new JSONObject();
        try{
            jsonRequest=parseRequest(request.getParameter("params"));
            String email= parseJsonAsEmail((String)jsonRequest.get("email"));
            String password=parseJsonAsPassword((String)jsonRequest.get("password"));
            user.setEmail(email);
            user.setPassword(password);
            userRepository.add(user);
        }catch(BusinessException b) {
            log.error("An error occurred while handling a CreateUser Request: {}.", jsonRequest.toJSONString(), b);
            errorMsg = "Error. " + b.getMessage();
            errorCode = b.getError().getCode();
            error = true;
        }catch(SystemException s){
            log.error("An error occurred while handling a CreateUser Request: {}.", jsonRequest.toJSONString(), s);
            errorMsg = "Error. " + s.getMessage();
            errorCode = s.getError().getCode();
            error = true;
        }

        JSONObject jsonResponse = new JSONObject();
        if (error){
            jsonResponse.put("error", ErrorHelper.createErrorJson(errorCode, errorMsg));
        }else {
            jsonResponse.put("success", true);
            jsonResponse.put("User", user.toJson());
        }
        sendMessage(jsonResponse, response);
    }
}
