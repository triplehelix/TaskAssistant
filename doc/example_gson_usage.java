
// http://edwin.baculsoft.com/2011/11/how-to-create-a-simple-servlet-to-handle-json-requests/


package com.edw.servlet;
 
import com.edw.bean.Status;
import com.edw.bean.Student;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class JsonParserServlet extends HttpServlet {
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");       
        Gson gson = new Gson();
 
        try {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }
 
            Student student = (Student) gson.fromJson(sb.toString(), Student.class);
 
            Status status = new Status();
            if (student.getName().equalsIgnoreCase("edw")) {
                status.setSuccess(true);
                status.setDescription("success");
            } else {
                status.setSuccess(false);
                status.setDescription("not edw");
            }
            response.getOutputStream().print(gson.toJson(status));
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            Status status = new Status();
            status.setSuccess(false);
            status.setDescription(ex.getMessage());
            response.getOutputStream().print(gson.toJson(status));
            response.getOutputStream().flush();
        }
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}