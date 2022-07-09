package kitchenpos.ui;

import kitchenpos.exception.TableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableControllerAdvice {
    public final Logger logger = LoggerFactory.getLogger(TableControllerAdvice.class);
    public final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(TableException.class)
    public ResponseEntity<String> productException(final TableException tableException) {
        logger.warn("tableException : " + tableException.getMessage());
        fileLogger.debug(tableException.getMessage());
        return ResponseEntity.badRequest().build();
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(final Exception exception) {
        logger.warn("Exception : " + exception.getMessage());
        fileLogger.debug(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
