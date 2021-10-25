package top.maof.haikang.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import top.maof.haikang.model.Perm;
import top.maof.haikang.model.Role;
import top.maof.haikang.model.User;
import top.maof.haikang.service.UserService;
import top.maof.haikang.utils.JWTUtil;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * shiro自定义认证授权类
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("--------------授权-----------------");
        Integer id = Integer.parseInt(String.valueOf(principalCollection.getPrimaryPrincipal()));
        User user = userService.getWithRolePerm(id);
        List<Role> roles = user.getRoles();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<>();
        Set<String> permSet = new HashSet<>();
        if (roles != null && roles.size() > 0)
            roles.forEach((role) -> {
                roleSet.add(role.getLevel() + "");
                List<Perm> perms = role.getPerms();
                if (perms != null && perms.size() > 0)
                    perms.forEach(perm -> {
                        permSet.add(perm.getPermission());
                    });
            });
        simpleAuthorizationInfo.setRoles(roleSet);
        simpleAuthorizationInfo.setStringPermissions(permSet);
        return simpleAuthorizationInfo;
    }


    private final static String SALT = "maofeng";

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken instanceof JWTToken) {
            log.info("--------------认证-----------------");
            // 携带token验证
            JWTToken jwtToken = (JWTToken) authenticationToken;
            if (JWTUtil.checkToken(jwtToken.getToken())) {
                // 此处不做token真伪校验,走个过场
                Md5Hash md5Hash = new Md5Hash(jwtToken.getToken(), SALT, 1);
                return new SimpleAuthenticationInfo(jwtToken.getPrincipal(), md5Hash.toHex(), ByteSource.Util.bytes(SALT), getName());
            }
        }
        return null;
    }
}
