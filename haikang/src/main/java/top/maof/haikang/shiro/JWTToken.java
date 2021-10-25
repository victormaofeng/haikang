package top.maof.haikang.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import top.maof.haikang.utils.JWTUtil;


public class JWTToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /**
     * 获取用户身份id
     * @return
     */
    @Override
    public Object getPrincipal() {
        return JWTUtil.parseToken(token, "id");
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
