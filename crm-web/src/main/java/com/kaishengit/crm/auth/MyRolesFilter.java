package com.kaishengit.crm.auth;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 自定义过滤器，用来满足多个角色的关系
 * @author Tonglin
 */
public class MyRolesFilter extends RolesAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request,
                                   ServletResponse response,
                                   Object mappedValue) throws IOException {

        //获取当前登录的对象
        Subject subject = getSubject(request,response);

        //获取配置文件中传入的角色列表
        String[] rolesName = (String[]) mappedValue;

        if (rolesName == null || rolesName.length == 0) {
            //所有角色都可以访问
            return true;
        }
        for (String role : rolesName) {
            if (subject.hasRole(role)) {
                //如果用于任意一个角色，就可以访问
                return true;
            }
        }
        return false;
    }
}
