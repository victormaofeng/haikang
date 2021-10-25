package top.maof.haikang.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * shiro自定义restful风格path解析器
 */
@Slf4j
public class RestFulPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    public RestFulPathMatchingFilterChainResolver() {
        super();
    }

    public RestFulPathMatchingFilterChainResolver(FilterConfig filterConfig) {
        super(filterConfig);
    }


    /* *
     * @Description 重写filterChain匹配
     * @Param [request, response, originalChain]
     * @Return javax.servlet.FilterChain
     */
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);
        // 去除请求地址的多余的 /
        if (requestURI.endsWith("/")) requestURI = requestURI.substring(0, requestURI.length() - 1);
        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        // log.info("requestURI:" + requestURI);
        for (String pathPattern : filterChainManager.getChainNames()) {
            // log.info("pathPattern:" + pathPattern);
            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathPattern != null && pathPattern.contains("==")) {
                String[] s = pathPattern.split("==");
                if (pathMatches(s[0], requestURI) && WebUtils.toHttp(request).getMethod().toUpperCase().equals(s[1].toUpperCase())) {
                    if (log.isTraceEnabled()) {
                        log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                                "Utilizing corresponding filter chain...");
                    }
                    FilterChain proxy = filterChainManager.proxy(originalChain, pathPattern);
                    return proxy;
                }
            } else {
                if (pathMatches(pathPattern, requestURI)) {
                    if (log.isTraceEnabled()) {
                        log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                                "Utilizing corresponding filter chain...");
                    }
                    // log.info("FilterChain");
                    return filterChainManager.proxy(originalChain, pathPattern);
                }
            }

        }
        return null;
    }
}
