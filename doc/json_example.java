import java.util.ArrayList;
import com.google.appengine.repackaged.com.google.gson.Gson;

/**
 *
 *
 * Expected output:
 *     [0,0,1,1,3,3,8,8,21,21,55,55]
 *     {"myIntegers":[0,0,1,1,3,3,8,8,21,21,55,55]}
 *     
 *     Process finished with exit code 0
 *
 * Created by kennethlyon on 8/10/16.
 */
public class Play {
    ArrayList<Integer> myIntegers;
    public static void main(String[]args) {
        Play play = new Play();
        Gson gson=new Gson();
        System.out.println(gson.toJson(play.myIntegers));
        System.out.println(gson.toJson(play));

    }
    Play(){
    myIntegers = new ArrayList<Integer>();
        int j=1;
        int k=0;
        for(int i=0; i<99;i+=j){
            myIntegers.add(i);
            j+=i;
            myIntegers.add(i);
        }
        Gson gson=new Gson();
      //  System.out.println(gson.toJson(myIntegers));
    }
}
