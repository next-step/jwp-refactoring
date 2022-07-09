package kitchenpos.ui;

import kitchenpos.exception.MenuException;
import kitchenpos.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuControllerAdvice {
    public final Logger logger = LoggerFactory.getLogger(MenuControllerAdvice.class);
    public final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> menuException(final MenuException menuException) {
        logger.warn("MenuException : " + menuException.getMessage());
        fileLogger.debug(menuException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> productException(final ProductException productException) {
        logger.warn("productException : " + productException.getMessage());
        fileLogger.debug(productException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(final Exception exception) {
        logger.warn("Exception : " + exception.getMessage());
        fileLogger.debug(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
