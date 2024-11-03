package my.pastebin.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class MyLogger {

    private static final Logger logger = LoggerFactory.getLogger(MyLogger.class);

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logWarn(String message) {
        logger.warn(message);
    }

    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void logRequest(String method, String url) {
        logger.info("Received request: Method = {}, URL = {}", method, url);
    }

    public void logResponse(String url, int statusCode, long executionTime) {
        logger.info("Response for URL = {}: Status = {}, Execution Time = {}ms", url, statusCode, executionTime);
    }
}
