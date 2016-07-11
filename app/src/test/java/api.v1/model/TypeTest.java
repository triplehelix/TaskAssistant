package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves a a container for test case proto-Types.
 * Created by kennethlyon on 6/9/16.
 */
public class TypeTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TypeTest.class);
    private static ArrayList<String> validTypes;
    private static ArrayList<String> errorTypes;
    private static ArrayList<String> validUpdates;
    private static ArrayList<String> errorUpdates;

    static {

        /* Add valid Types. Types fields are arranged in the order:
         * validTypes.add("int id`);
         */
        validTypes = new ArrayList<String>();
        validUpdates = new ArrayList<String>();
        errorTypes = new ArrayList<String>();
        errorUpdates = new ArrayList<String>();
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        validTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");
        errorTypes.add("");

        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");
        validUpdates.add("");

        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
        errorUpdates.add("");
    }

    /**
     *
     * @return
     */
    private static JSONObject toJson(String stringType) {
        String[] TypeElementArray = stringType.split("`");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",   TypeElementArray[0]);
        LOGGER.info("Created request {}", jsonObj.toJSONString());
        return jsonObj;
    }

    /**
     *
     * @return
     */
    public static ArrayList<JSONObject> getValidTestTypesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

        for (String s : validTypes)
            jsonObjectArrayList.add(TypeTest.toJson(s));
        return jsonObjectArrayList;
    }

    /**
     *
     * @return
     */
    public static ArrayList<JSONObject> getErrorTestTypesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorTypes)
            jsonObjectArrayList.add(TypeTest.toJson(s));
        return jsonObjectArrayList;

    }

    /**
     *
     * @return
     */
    public static ArrayList<JSONObject> getValidTestTypeUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : validUpdates)
            jsonObjectArrayList.add(TypeTest.toJson(s));
        return jsonObjectArrayList;
    }

    /**
     *
     * @return
     */
    public static ArrayList<JSONObject> getErrorTestTypeUpdatesAsJson() {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        for (String s : errorUpdates)
            jsonObjectArrayList.add(TypeTest.toJson(s));
        return jsonObjectArrayList;
    }


    /**
     *
     * @return
     */
    public static ArrayList<Type> getValidTestTypesAsTypes() throws Exception{
        ArrayList<Type> TypeArrayList = new ArrayList<Type>();
        for (String s : validTypes) {
            TypeArrayList.add(TypeTest.toType(s));
        }
        return TypeArrayList;
    }

    /**
     *
     * @return
     */
    public static ArrayList<Type> getValidTestTypesUpdatesAsTypes() throws Exception{
        ArrayList<Type> TypeArrayList = new ArrayList<Type>();
        for (String s : validUpdates) {
            TypeArrayList.add(TypeTest.toType(s));
        }
        return TypeArrayList;
    }

    /**
     *
     * @return
     */
    private static Type toType(String s) throws Exception{
        String[] TypeElementArray = s.split("`");
        Type Type = new Type();
            Type.setId(Integer.parseInt(TypeElementArray[0]));
        return Type;
    }

    /**
     * Parse a String representing a given date and return a Date object.
     * String must be in the format: yyyy-MM-dd_HH:mm:ss
     *
     * @param stringDate
     * @return
     */
    private static Date parseJsonDateAsDate(String stringDate) throws BusinessException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        df.setLenient(false);
        Date result = null;
        try {
            result = df.parse(stringDate);
        } catch (java.text.ParseException e) {
            LOGGER.error("Exception while parsing date token: " + stringDate, e);
            throw new BusinessException("Error caused by the String date: " + stringDate, Error.valueOf("PARSE_DATE_ERROR"));
        }
        return result;
    }


    /**
     * Parse string as boolean.
     * @param b
     * @return
     */
    private static boolean parseJsonBooleanAsBoolean(String b) throws BusinessException{
        b = b.trim().toUpperCase();
        if (b.equals("TRUE"))
            return true;
        else if(b.equals("FALSE"))
            return false;
        else
            throw new BusinessException("Invalid boolean value: " + b, Error.valueOf("PARSE_BOOLEAN_ERROR"));
    }


    /**
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        for(String s: validTypes){
            TypeTest.toType(s);
            LOGGER.info("Valid Type {}", toJson(s));
        }

        for(String s: validUpdates){
            TypeTest.toType(s);
            LOGGER.info("Valid Type {}", toJson(s));
        }

        for(String s: errorTypes){
            validateErrorType(s);
            LOGGER.info("Error Type {}", toJson(s));
        }

        for(String s: errorUpdates){
            validateErrorType(s);
            LOGGER.info("Error Type {}", toJson(s));
        }

    }

    public void validateErrorType(String s){
        boolean error=false;
        try{
            TypeTest.toType(s);
        }catch(Exception e){
            error=true;
            LOGGER.info("Invalid Type returned error. " + e.getMessage(), e);
        }
        if(!error){
            fail("Success returned for invalid Type: " + s);
        }
    }
}
