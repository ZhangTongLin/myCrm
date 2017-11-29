package com.kaishengit.crm.controller;

import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.entity.Staff;

import javax.servlet.http.HttpSession;

/**
 * 所有Controller层的父类，定义了共有的方法
 * @author Administrator.
 */
public abstract class BaseController {

    /**
     * 获取当前登录的账户
     * @param session
     * @return 当前登录的账号
     */
    public static Staff getCurrentAccount(HttpSession session) {
        return (Staff) session.getAttribute("curr_account");
    }

    public static Staff getCurrentStaff(){
        return ShiroUtil.getStaff();
    };

}
