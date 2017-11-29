package com.kaishengit.crm.controller;


import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.entity.Staff;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录的控制层
 * @author Administrator.
 */
@Controller
@RequestMapping("/")
public class LoginController {


    @GetMapping
    public String staffLogin() {

        Subject subject = ShiroUtil.getSubject();

        if (subject.isAuthenticated()) {
            //被认证了，再次回到登录页面，认为要切换账号
            subject.logout();
        }

        if (subject.isRemembered()) {
            //被记住的用户直接跳转登录成功页面
            return "redirect:/home";
        }

        return "login";
    }

    @PostMapping
    public String staffLogin(String userName,
                             String password,
                             boolean rememberMe,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {

        try {

            Subject subject = ShiroUtil.getSubject();
            UsernamePasswordToken usernamePasswordToken =
                    new UsernamePasswordToken(userName,new Md5Hash(password).toString(),rememberMe);
            subject.login(usernamePasswordToken);

            Staff staff = (Staff) subject.getPrincipal();
            Session session = subject.getSession();
            session.setAttribute("curr_account",staff);

            //登录后直接跳转到登录前的页面
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);

            String url = "/home";
            if (savedRequest != null) {
                //获取登录前的url
                url = savedRequest.getRequestUrl();
            }

            return "redirect:" + url;

        } catch (AuthenticationException ex) {
            redirectAttributes.addFlashAttribute("message","账号或者密码错误");
            return "redirect:/";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {

        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message","您已安全退出");
        return "redirect:/";
    }
}
