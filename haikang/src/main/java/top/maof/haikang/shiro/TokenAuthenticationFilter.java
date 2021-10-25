package top.maof.haikang.shiro;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import top.maof.haikang.result.Result;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * token认证过滤器
 */
@Slf4j
public class TokenAuthenticationFilter extends BasicHttpAuthenticationFilter {

    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // log.info("-----------------token认证过滤器----------------");
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            PrintWriter writer;
            response.setCharacterEncoding("utf-8");
            writer = httpServletResponse.getWriter();
            writer.print(JSON.toJSONString(Result.response_401()));
            writer.flush();
            writer.close();
        }
        return false;
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // token将封装在请求头中
        // String token1 = httpServletRequest.getHeader("token");
        // token 封装在请求体中,方便取用户id
         String token1 = httpServletRequest.getParameter("token");
        log.info("token:" + token1);
        JWTToken jwtToken = new JWTToken(token1);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = getSubject(request, response);
        subject.login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        if (requestURI.endsWith("/")) requestURI = requestURI.substring(0, requestURI.length() - 1);
        String[] strings = path.split("==");

        if (strings.length <= 1) {
            // 普通的 URL, 正常处理
            return this.pathsMatch(strings[0], requestURI);
        } else {
            // 获取当前请求的 http method.
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();

            // 匹配当前请求的 http method 与 过滤器链中的的是否一致
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
        }
    }
}
