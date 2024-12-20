package my.pastebin.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        request.setAttribute("startTime", System.currentTimeMillis());
        MyLogger.logRequest(method, url);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        int status = response.getStatus();
        long startTime = (Long) request.getAttribute("startTime");
        long executionTime = System.currentTimeMillis() - startTime;
        MyLogger.logResponse(url, status, executionTime);
    }
}
