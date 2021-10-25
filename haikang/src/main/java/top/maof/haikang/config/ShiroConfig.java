package top.maof.haikang.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import top.maof.haikang.shiro.MyRealm;
import top.maof.haikang.shiro.RedisCacheManager;
import top.maof.haikang.shiro.RestShiroFilterFactoryBean;
import top.maof.haikang.shiro.TokenAuthenticationFilter;


import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限框架shiro配置
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/24 15:36
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        shiroFilterFactoryBean.setLoginUrl("/login.html");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(16);

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new TokenAuthenticationFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        // 过滤链定义，从上向下顺序执行，一般将放在最为下边

        filterChainDefinitionMap.put("/book/*==delete", "jwt");
        filterChainDefinitionMap.put("/book==put", "jwt");
        filterChainDefinitionMap.put("/book==post", "jwt");

        // 其他请求一律放行
        filterChainDefinitionMap.put("/**", "anon");

        // 所有url都必须认证通过才可以访问
        // filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
      /*  DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);*/
        return defaultWebSecurityManager;
    }

    @Bean
    public MyRealm realm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        MyRealm myRealm = new MyRealm();
        // 设置加密策略
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        // 开启缓存
        myRealm.setCacheManager(new RedisCacheManager());
        myRealm.setCachingEnabled(true);
        return myRealm;
    }


    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }
}
