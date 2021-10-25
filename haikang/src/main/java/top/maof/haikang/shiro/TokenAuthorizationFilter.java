package top.maof.haikang.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * token授权过滤器
 */
@Slf4j
public class TokenAuthorizationFilter extends RolesAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        log.info("-----------------token授权过滤器----------------");
        Subject subject = this.getSubject(request, response);
        String[] rolesArray = ((String[]) mappedValue);
        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            return subject.hasAllRoles(roles);
        } else {
            return true;
        }
    }
}
