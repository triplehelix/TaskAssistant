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
	
	/**
	 * Parse a String representation of an integer as an Integer object. A
	 * null Integer indicates that a NumberFormatException has occurred.
	 * 
	 * @param i
	 * @return
	 */
	protected Integer parseJsonIntAsInt(String i){
		Integer myInt = null;
		try{
			myInt = Integer.parseInt(i);
		}catch(NumberFormatException e){
			log.error("Exception while parsing integer token: " + i);
		}
		return myInt;
	}
}
