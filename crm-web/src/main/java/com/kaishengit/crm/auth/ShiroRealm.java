package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Department;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.service.StaffService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tonglin
 */
public class ShiroRealm extends AuthorizingRealm {

    private StaffService staffService;

    /**
     * 验证权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        Staff staff = (Staff) principalCollection.getPrimaryPrincipal();

        //根据当前的用户，查看用户所在部门
        List<Department> departmentList = staffService.findDeptByStaffId(staff.getId());

        List<String> nameList = new ArrayList<>();

        for (Department department : departmentList) {
            nameList.add(department.getDeptName());
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //赋予角色
        simpleAuthorizationInfo.addRoles(nameList);
        //赋予权限
        //simpleAuthorizationInfo.setStringPermissions();

        return simpleAuthorizationInfo;
    }

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        Staff staff = staffService.findByPhoneNum(userName);

        if (staff != null) {
            return new SimpleAuthenticationInfo(staff,staff.getPassword(),getName());
        }
        return null;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }
}
