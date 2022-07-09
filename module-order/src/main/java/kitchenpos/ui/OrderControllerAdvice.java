package kitchenpos.ui;

import kitchenpos.exception.MenuException;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.TableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerAdvice {
    public final Logger logger = LoggerFactory.getLogger(OrderControllerAdvice.class);
    public final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> orderException(final OrderException orderException) {
        logger.warn("orderException : " + orderException.getMessage());
        fileLogger.debug(orderException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableException.class)
    public ResponseEntity<String> tableException(final TableException tableException) {
        logger.warn("tableException : " + tableException.getMessage());
        fileLogger.debug(tableException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> menuException(final MenuException menuException) {
        logger.warn("menuException : " + menuException.getMessage());
        fileLogger.debug(menuException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(final Exception exception) {
        logger.warn("Exception : " + exception.getMessage());
        fileLogger.debug(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
