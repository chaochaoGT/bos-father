package cn.ssh.bos.shiro.realm;

import cn.ssh.bos.domain.auth.Function;
import cn.ssh.bos.domain.auth.Role;
import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static java.awt.SystemColor.info;
import static org.apache.shiro.web.filter.mgt.DefaultFilter.roles;

/**
 * Created by Administrator on 2017/7/30.
 */
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private FacadeService facadeService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        Subject sub= SecurityUtils.getSubject();
        User user= (User) sub.getPrincipal();
        if ("123@123.com".equals(user.getEmail())) {
            //超级管理员
            //获取所有角色
            List<Role> role=facadeService.getRoleService().findAll();
            //获取所有权限
            for (Role r: role) {
                info.addRole(r.getCode());
            }

            List<Function> func=facadeService.getFunctionService().findAll();
            for (Function f: func) {
                info.addStringPermission(f.getCode());
            }

        }else{
            //普通用户
            List<Role> roles=facadeService.getRoleService().findAllRoles(user.getId());
            for (Role r:roles) {
                info.addRole(r.getCode());
                Set<Function> func = r.getFunctions();
                for (Function f: func) {
                    info.addStringPermission(f.getCode());
                }
            }
        }
        return info;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken token1= (UsernamePasswordToken) token;
        String username = token1.getUsername();
                User exitUser=facadeService.getUserService().findByEmial(username);
        if (exitUser == null) {
            return null;
        } else {
            // shiro 比对 密码 数据库密码提供即可
            // 参数一 用户对象 后续 通过Subject.getPricinpal()获取 existUser对象
            // 参数二 : 证书 数据库密码
            // 参数三 : realm注册名称 id值 通过 super.getName() 获取注册realm名称
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(exitUser, exitUser.getPassword(), super.getName());
            return info;
        }

    }






}
