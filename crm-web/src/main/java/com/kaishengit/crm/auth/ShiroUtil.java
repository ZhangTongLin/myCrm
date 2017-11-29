package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Staff;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author Tonglin
 */
public class ShiroUtil {

    public static Staff getStaff() {
        return (Staff) getSubject().getPrincipal();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

}
