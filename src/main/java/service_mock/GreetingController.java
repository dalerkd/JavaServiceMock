package service_mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class GreetingController {


    private static HashMap<String,MockDataManager> DataHM = new HashMap<>();

    public GreetingController() {
        MockDataManager mockDataManager = new MockDataManager();
        mockDataManager.SetMethodJson("GET","{\n" +
                "    \"code\": 200, \n" +
                "    \"msg\": \"操作成功\", \n" +
                "    \"data\": \"http://www.kuaibi888.com\"\n" +
                "}","200");
        DataHM.put("/api/anon/setting/get.do",mockDataManager);
    }

    /*
    * mock:
    * GET /mocks    -- 列出所有mocks
    * GET /mocks/<path> -- 查询指定path的mocks信息
    * PUT /mocks/<path> -- 更新指定path的mocks信息,注意是幂等的
    * POST/mocks        -- 创建一个新的mock                    √
    * DELETE /mocks     -- 删除一个账户
    *
    *
    *
    * */

    /*
    * @param path:      你要mock的webPath 比如 /a/b.do  那么就是它了
    * @param method:    你要mock的method:是这几者之一:GET,POST,PUT,DELETE
    * @param returnJson: 你使用本函数的原因
    * @param mockStatus: 默认是200,除非你想要服务器直接返回错误的状态码
    * //能否对超时请求进行测试?
    *
    * */

    @PostMapping(value = "/mocks")
    public void CreateMocks(
                            @RequestParam(value = "mockPath")
                                    String mockPath,
                            @RequestParam(value = "mockMethod")
                                    String mockMethod,
                            @RequestParam(value = "returnJson",required = false,defaultValue="{\"tips\",\"你不应该看到这个界面\"}")
                                    String returnJson,
                            @RequestParam(value = "returnStatus",required = false,defaultValue = "200")
                                    String returnStatus
    ){
        /*
        * method
        * json
        * */
        System.out.println(mockPath);
        System.out.println(mockMethod);
        System.out.println(returnJson);
        System.out.println(returnStatus);

        AddUpdateMockData(mockPath,mockMethod,returnJson,returnStatus);

    }
    private void AddUpdateMockData(String mockPath,String mockMethod,String returnJson,String returnStatus){
         MockDataManager mdm = DataHM.get(mockPath);
        if (null!=mdm){
            mdm.SetMethodJson(mockMethod,returnJson,returnStatus);
        }else{
            mdm = new MockDataManager();
            mdm.SetMethodJson(mockMethod,returnJson,returnStatus);
        }
        DataHM.put(mockPath,mdm);
    }





    /*
    * Spring使用jackson处理,无法处理其他种类的json(eg:JSONObject).
    * 所以由 Spring转换成Map然后返回即可
    * 只所以没有进行处理是因为
    * */

    @RequestMapping(value = "/**")
     public ResponseEntity<Map<String, Object>> DecideLevel(
             HttpServletRequest request){
        return MockDecide(request.getServletPath(),request.getMethod());
    }

    private ResponseEntity<Map<String,Object>> MockDecide(String webPath,String method){
        MockDataManager mdm = DataHM.get(webPath);
        if(null==mdm){
            return null;
        }
        String jsonString = mdm.GetMethodJson(method);
        ObjectMapper mapper = new ObjectMapper();
        Map mJson = null;
        try {
            mJson = mapper.readValue(jsonString,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String StrStatus =  mdm.GetMethodStatus(method);
        Integer states  = Integer.parseInt(StrStatus);
       return new ResponseEntity<Map<String,Object>>(mJson, HttpStatus.valueOf(states));  // m;

    }


}
/*
* method:  post delete put get
* 操作:
*   - 返回错误
*   - 返回指定json数据
*
* */