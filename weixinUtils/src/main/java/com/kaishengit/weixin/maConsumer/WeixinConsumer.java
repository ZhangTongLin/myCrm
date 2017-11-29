package com.kaishengit.weixin.maConsumer;

import com.alibaba.fastjson.JSON;
import com.kaishengit.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator.
 */
@Component
public class WeixinConsumer {

    @Autowired
    private WeixinUtil weixinUtil;

    @JmsListener(destination = "weixinMessage-queue")
    public void sendMessageToWeixin(String json) {

        Map<String,Object> map = JSON.parseObject(json, HashMap.class);
        weixinUtil.sendMessage(Arrays.asList(12,21),map.get("message").toString());

    }
}
