package api.v1.helper;

import api.v1.error.BusinessException;
import api.v1.error.SystemException;

import java.util.ArrayList;

/**
 * Created by kennethlyon on 9/23/16.
 */
public class AccessPrivilegesHelper {



    protected void verifyCategoryOwnership(int userId, ArrayList<Integer> categoryIds) throws BusinessException, SystemException {
    }

    protected void verifyTaskOwnership(int userId, ArrayList<Integer> categoryIds) throws BusinessException, SystemException{
    }

    protected void verifyScheduleOwnership(int userId, ArrayList<Integer> categoryIds) throws BusinessException, SystemException{
    }

    protected void verifyTaskListOwnership(int userId, ArrayList<Integer> categoryIds) throws BusinessException, SystemException{
    }


}
