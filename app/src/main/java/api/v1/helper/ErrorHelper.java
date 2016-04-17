package api.v1.helper;

import org.json.simple.JSONObject;

/**
 * Created by mikeh on 4/17/2016.
 */
public class ErrorHelper {

    public static JSONObject createErrorJson(int code, String msg){
        JSONObject errorObj = new JSONObject();
        errorObj.put("code", code);
        errorObj.put("msg", msg);
        return errorObj;
    }
}
