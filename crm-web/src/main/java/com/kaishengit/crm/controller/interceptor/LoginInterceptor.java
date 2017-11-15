package com.kaishengit.crm.controller.interceptor;

import com.kaishengit.crm.entity.Admin;
import com.kaishengit.crm.entity.Staff;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {


        String url = request.getRequestURI();

        if (url.startsWith("/static/")) {
            return true;
        }

        if ("".equals(url) || "/".equals(url)) {
            return true;
        }

        HttpSession session = request.getSession();

        Staff staff = (Staff) session.getAttribute("curr_account");

        if (staff != null) {
            return true;
        }
        response.sendRedirect("/");
        return false;
    }
}
