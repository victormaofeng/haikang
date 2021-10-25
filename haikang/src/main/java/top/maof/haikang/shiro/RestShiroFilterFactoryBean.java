package top.maof.haikang.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * 自定义ShiroFilterFactoryBean,引入自定义shiro解析链
 */
@Slf4j
public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    public RestShiroFilterFactoryBean() {
        super();
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        log.debug("Creating Shiro Filter instance.");

        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }

        FilterChainManager manager = createFilterChainManager();

        //Expose the constructed FilterChainManager by first wrapping it in a
        // FilterChainResolver implementation. The AbstractShiroFilter implementations
        // do not know about FilterChainManagers - only resolvers:

        // 自定义拦截器解析链
        RestFulPathMatchingFilterChainResolver chainResolver = new RestFulPathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        //Now create a concrete ShiroFilter instance and apply the acquired SecurityManager and built
        //FilterChainResolver.  It doesn't matter that the instance is an anonymous inner class
        //here - we're just using it because it is a concrete AbstractShiroFilter instance that accepts
        //injection of the SecurityManager and FilterChainResolver:
        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    private static final class SpringShiroFilter extends AbstractShiroFilter {

        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }

}