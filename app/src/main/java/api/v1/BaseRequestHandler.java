package api.v1;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import api.v1.error.SystemException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import api.v1.error.BusinessException;
import org.slf4j.LoggerFactory;
import api.v1.error.Error;
import org.slf4j.Logger;

/**
 * This class is used by all handlers to parse the JSONObject referred
 * to as the JSONObject.
 * @author kennethlyon
 *
 */
public class BaseRequestHandler extends HttpServlet{

    protected static final Logger log = LoggerFactory.getLogger(BaseRequestHandler.class);
    private final static String DATE_FORMAT_KEY="yyyy-MM-dd_HH:mm:ss";

	/**
	 *
	 * @param requestString
	 * @return
	 * @throws BusinessException
     */
    protected JSONObject parseRequest(String requestString)  throws BusinessException {
	JSONObject param = null;
	try{
	    JSONParser parser = new JSONParser();
	    param =  (JSONObject) parser.parse(requestString);
	}catch(ParseException e){
	    log.error("Exception while parsing request: " + requestString);
	    throw new BusinessException ("Error caused by: " + requestString, Error.valueOf("PARSE_JSON_EXCEPTION"));
	}
	return param;
    }
	
	/**
	 * Incomplete. Always returns true.
	 * @param credential
	 * @return
	 */
	protected boolean validateCredentials(Subject credential){
		//TODO return false sometimes.
		return true;
	}
	
	/**
	 * Parse a String representing a given date and return a Date object.
	 * String must be in the format: yyyy-MM-dd_HH:mm:ss
	 * @param stringDate
	 * @return
	 */
	protected Date parseJsonDateAsDate(String stringDate) throws  BusinessException{
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_KEY);
		Date result = null;
		try{
			result = df.parse(stringDate);
		} catch (java.text.ParseException e) {
			log.error("Exception while parsing date token: " + stringDate);
			throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
		}
			return result;
	}
	
	/**
	 * Parse a String representation of an integer as an Integer object. A
	 * null Integer indicates that a NumberFormatException has occurred.
	 * 
	 * @param i
	 * @return
	 */
	protected Integer parseJsonIntAsInt(String i) throws BusinessException {
        Integer myInt=0;
        String nfeError="Exception while parsing the token as an integer: " + i;
		try{
			myInt = Integer.parseInt(i);
		}catch(NumberFormatException e){
            log.error(nfeError);
            throw new BusinessException(nfeError, Error.valueOf("PARSE_INTEGER_EXCEPTION"));
        }
		return myInt;
	}

	/**
	 * This method sends success/failure response back to the web layer that 
	 * called the given servlet subclass. It also logs an error. 
	 * 
	 * @param error
	 * @param message
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	protected static void sendResponse(boolean error, String message, HttpServletResponse response) throws IOException{
		JSONObject obj = new JSONObject();
		obj.put("error", error);
		obj.put("errorMsg", message);
		PrintWriter out = response.getWriter();
		out.println(obj);
	}

    /**
     *
     * @param response
     * @param httpResponse
     * @throws IOException
     */
	protected static void sendMessage(JSONObject response, HttpServletResponse httpResponse) throws IOException{
        PrintWriter out = httpResponse.getWriter();
        out.println(response);
	}

    /**
     * Parse a String as a long integer.
     * @param l
     * @return
     */
    protected long parseJsonLongAsLong(String l) throws BusinessException{
        l=l.trim();
        long myLong=0;
        String nfeError="Exception while parsing the token as a long integer: " + l;
        try{
            myLong=java.lang.Long.parseLong(l);
        }catch(NumberFormatException nfe){
            log.error(nfeError);
            throw new BusinessException(nfeError, Error.valueOf("PARSE_LONG_INTEGER_ERROR"));
        }
        return myLong;
    }

    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    protected boolean parseJsonBooleanAsBoolean(String b) throws BusinessException{
        log.debug("The passed to BaseRequestHandler.parseJsonBooleanAsBoolean is " + b + ".");
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }
}