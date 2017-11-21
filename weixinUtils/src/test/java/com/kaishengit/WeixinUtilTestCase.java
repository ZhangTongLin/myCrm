package com.kaishengit;

import com.kaishengit.weixin.WeixinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * @author Administrator.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-weixin.xml")
public class WeixinUtilTestCase {

    @Autowired
    private WeixinUtil weixinUtil;

    @Test
    public void getAccessTokenTest() {
        String accessToken = weixinUtil.getAccessToken(WeixinUtil.TYPE_ACCESS_TOKEN_CONTACTS);

        System.out.println(accessToken);
    }

    @Test
    public void addDepartment() {
        weixinUtil.addDepartMent("销售部",101,100);
    }

    @Test
    public void deleteDepartment() {
        weixinUtil.deleteDepartment(101);
    }


    @Test
    public void addMember() {
        weixinUtil.addMember(11,"tom",Arrays.asList(100),"18263817933");
    }

    @Test
    public void deleteMember() {
        weixinUtil.deleteMember(11);
    }

    @Test
    public void sendMessage() {
        weixinUtil.sendMessage(null,"hello,tonglin");
    }

}
