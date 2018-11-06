package service_mock;

import java.io.Serializable;
import java.util.HashMap;

public class MockDataManager {

/*
* @param String method
* @param String returnJson
* */
    private HashMap<String, String> methodHM;
    private HashMap<String, String> statusHM;
    /*
    * 获取方法对应的json
    * */

    public MockDataManager(){
        methodHM = new HashMap<>();
        statusHM = new HashMap<>();
    }

    public String GetMethodJson(String method){
        return methodHM.get(method);
    }
    /*
    * 服务器直接返回的http status
    * */
    public String GetMethodStatus(String method){
        return statusHM.get(method);
    }
    public void SetMethodJson(String method,String json,String mockStatus){
        methodHM.put(method,json);
        statusHM.put(method,mockStatus);
    }



    public HashMap<String, String> getMethodHM() {
        return methodHM;
    }

    public HashMap<String, String> getStatusHM() {
        return statusHM;
    }
}



