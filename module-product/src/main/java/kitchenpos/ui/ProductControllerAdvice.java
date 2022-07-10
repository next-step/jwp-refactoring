package kitchenpos.ui;

import kitchenpos.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {
    public final Logger logger = LoggerFactory.getLogger(ProductControllerAdvice.class);
    public final Logger fileLogger = LoggerFactory.getLogger("file");

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
