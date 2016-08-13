package api.v1.model;

import api.v1.error.BusinessException;
import api.v1.error.Error;
import com.google.appengine.repackaged.com.google.gson.Gson;

/**
 * Created by kennethlyon on 6/25/16.
 */
public class Type {

    private int id;
    public Type(){
        this.id=-1;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    /**
     * Create a serialized JSON String of this instance
     * using GSON.
     * @return
     */
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
}
