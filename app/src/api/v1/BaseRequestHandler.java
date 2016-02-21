package api.v1;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServlet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by all handlers to parse the JSONObject referred
 * to as the JSONObject.
 * @author kennethlyon
 *
 */
public class BaseRequestHandler extends HttpServlet{
private static final Logger log = LoggerFactory.getLogger(BaseRequestHandler.class);
private final static String DATE_FORMAT_KEY="yyyy-MM-dd_HH:mm:ss";		
	
protected JSONObject parseRequest(String requestString){
		JSONObject param = null;		
		try{
			JSONParser parser = new JSONParser();
			param =  (JSONObject) parser.parse(requestString);
		}catch(org.json.simple.parser.ParseException e){
			log.error("Exception while parsing request: " + requestString);
		}
		return param;
	}
	
	protected boolean validateCredentials(Subject credential){
		//TODO return false sometimes.
		return true;
	}
	
	/**
	 * 
	 * @param stringDate
	 * @return
	 */
	protected Date parseJsonDateAsDate(String stringDate){		
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_KEY);
		Date result = null;
		try{
			result = df.parse(stringDate);
		} catch (java.text.ParseException e) {			
			log.error("Exception while parsing date token: " + stringDate);
		}
			return result;
		
	}
}
