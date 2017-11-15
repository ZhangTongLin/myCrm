package com.kaishengit.crm.controller;


import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.exception.VerifyException;
import com.kaishengit.crm.service.AdminService;
import com.kaishengit.crm.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author Administrator.
 */
@Controller
@RequestMapping("/")
public class LoginController {


    @Autowired
    private StaffService staffService;
    @Autowired
    private AdminService adminService;


    @GetMapping
    public String staffLogin() {
        return "login";
    }

    @PostMapping
    public String staffLogin(String userName, String password,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        try {
            Staff staff = staffService.verify(userName,password);
            session.setAttribute("curr_account",staff);
            return "redirect:/home";

        } catch (VerifyException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message","您已安全退出");
        return "redirect:/";
    }
}
