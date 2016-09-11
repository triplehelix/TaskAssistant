package api.v1.helper;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 8/27/16.
 */
public class ModelHelper {
    /**
     * Create a deep copy of an ArrayList of Integers. This method is used
     * frequently to copy reference arrays to
     * @param integerArrayList
     * @return
     */
    public static ArrayList<Integer> copyIntegerArrayList(ArrayList<Integer> integerArrayList){
        ArrayList<Integer> copyOf_integerArrayList=new ArrayList<Integer>();
        if(integerArrayList==null)
            return null;
        else {
            for (Integer i : integerArrayList)
                copyOf_integerArrayList.add(new Integer(i));
            return copyOf_integerArrayList;
        }
    }

}
