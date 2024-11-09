package my.pastebin.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class MyLogger {
    private static final Logger logger = LoggerFactory.getLogger(MyLogger.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void logRequest(String method, String url) {
        logger.info("Received request: Method = {}, URL = {}", method, url);
    }

    public static void logResponse(String url, int statusCode, long executionTime) {
        logger.info("Response for URL = {}: Status = {}, Execution Time = {}ms", url, statusCode, executionTime);
    }
}
