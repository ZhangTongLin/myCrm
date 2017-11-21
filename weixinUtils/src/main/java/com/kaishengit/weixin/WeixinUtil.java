package com.kaishengit.weixin;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kaishengit.weixin.exception.WeixinException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信的工具类
 * @author Administrator.
 */
@Component
public class WeixinUtil {


    @Value("${weixin.corpID}")
    private String corpID;
    @Value("${weixin.normal.secret}")
    private String normalSecret;
    @Value("${weixin.contact.secret}")
    private String contactSecret;
    @Value("${weixin.agentId}")
    private Integer agentId;

    /**
     * 通讯录的accessToken的type
     */
    public static final String TYPE_ACCESS_TOKEN_CONTACTS = "contact";
    /**
     * 正常的accessToken的type
     */
    public static final String TYPE_ACCESS_TOKEN_NORMAL = "normal";
    /**
     * 获取access_token的URL
     */
    private static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    /**
     * 创建新的部门的URL
     */
    private static final String CREATE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";
    /**
     * 删除部门的URL
     */
    private static final String DELETE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";
    /**
     * 创建部门的新成员的url
     */
    private static final String CREATE_MEMBER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";
    /**
     * 删除成员的URL
     */
    private static final String DELETE_MEMBER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s";
    /**
     * 发送文本消息的URL
     */
    private static final String SEND_MESSAGE_URL = " https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";


    /**
     * accessToken的缓存
     */
    private LoadingCache<String,String> accessTokenCache = CacheBuilder.newBuilder()
            .expireAfterAccess(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String type) throws Exception {

                    String url = "";
                    if (type.equals(TYPE_ACCESS_TOKEN_NORMAL)) {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpID,normalSecret);
                    } else {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpID,contactSecret);
                    }

                    String resultJson = sendHttpGet(url);

                    Map<String,Object> resultMap = JSON.parseObject(resultJson, HashMap.class);

                    if (resultMap.get("errcode").equals(0)) {
                        return (String) resultMap.get("access_token");
                    }

                    throw new WeixinException("获取access_token异常");
                }
            });


    /**
     * 获取accessToken
     * @return 返回accessToken
     * @param type 想要获得的accessToken类型
     */
    public String getAccessToken(String type) {
        try {
            return accessTokenCache.get(type);
        } catch (ExecutionException e) {
            throw new WeixinException("获取access_token异常",e);
        }
    }


    /**
     *   * 创建一个部门
     * @param deptName
     * @param id 部门id
     * @param pId 父部门id
     */
    public void addDepartMent(String deptName,Integer id,Integer pId){

        String url = String.format(CREATE_DEPT_URL,getAccessToken(TYPE_ACCESS_TOKEN_CONTACTS));

        Map<String,Object> deptMap = new HashMap<String, Object>();
        deptMap.put("name",deptName);
        deptMap.put("parentid",pId);
        deptMap.put("id",id);

        String postResult = sendHttpPost(url,JSON.toJSONString(deptMap));
        Map<String,Object> resultMap = JSON.parseObject(postResult,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("创建部门失败" + postResult);
        }

    }

    /**
     * 根据id删除一个部门
     * @param id 部门主键
     */
    public void deleteDepartment(Integer id) {

        String url = String.format(DELETE_DEPT_URL,getAccessToken(TYPE_ACCESS_TOKEN_CONTACTS),id);
        String resultJson = sendHttpGet(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("删除部门失败" + resultJson);
        }

    }


    /**
     * 创建一个新的成员
     * @param userId 成员id
     * @param userName 成员姓名
     * @param deptList 部门的集合
     * @param mobile 手机号
     */
    public void addMember(Integer userId, String userName, List<Integer> deptList,String mobile) {

        String url = String.format(CREATE_MEMBER_URL,getAccessToken(TYPE_ACCESS_TOKEN_CONTACTS));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userid",userId);
        map.put("name",userName);
        map.put("mobile",mobile);
        map.put("department",deptList.toArray());

        String resultJson = sendHttpPost(url,JSON.toJSONString(map));
        Map<String,Object> resultMap = JSON.parseObject(resultJson);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("创建成员失败" + resultJson);
        }

    }

    /**
     * 删除对应的成员
     * @param id
     */
    public void deleteMember(Integer id) {

        String url = String.format(DELETE_MEMBER_URL,getAccessToken(TYPE_ACCESS_TOKEN_CONTACTS),id);
        String resultJson = sendHttpGet(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("删除成员失败" + resultJson);
        }

    }

    /**
     * 发送消息给成语昂
     * @param userIdList 成员id列表
     * @param message 发送的信息
     */
    public void sendMessage(List<Integer> userIdList,String message) {

        String url = String.format(SEND_MESSAGE_URL,getAccessToken(TYPE_ACCESS_TOKEN_NORMAL));

        StringBuilder builder = new StringBuilder();
        for (Integer userId : userIdList) {
            builder.append(userId).append("|");
        }

        String userIdString = builder.toString();
        userIdString = userIdString.substring(0,userIdString.lastIndexOf("|"));

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("touser", userIdString);
        map.put("msgtype","text");
        map.put("agentid",agentId);
        Map<String,String> contentMap = new HashMap<String, String>();
        contentMap.put("content",message);
        map.put("text",contentMap);

        String json = JSON.toJSONString(map);

        String resultJson = sendHttpPost(url,json);
        Map<String,Object> resultMap = JSON.parseObject(resultJson);

        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("消息发送失败" + resultJson);
        }

    }

    /**
     * 发出http get请求
     */
    private String sendHttpGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("发出http的get请求异常",e);
        }

    }

    /**
     * 发出http post请求
     */
    private String sendHttpPost(String url, String json) {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("发出http的post请求异常",e);
        }

    }

}
