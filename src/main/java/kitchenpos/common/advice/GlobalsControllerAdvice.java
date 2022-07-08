package kitchenpos.common.advice;

import kitchenpos.menu.exception.MenuException;
import kitchenpos.order.exception.OrderException;
import kitchenpos.product.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalsControllerAdvice {
    public final Logger logger = LoggerFactory.getLogger(GlobalsControllerAdvice.class);
    public final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> productException(final ProductException productException) {
        logger.warn("ProductException : " + productException.getMessage());
        fileLogger.debug(productException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> menuException(final MenuException menuException) {
        logger.warn("MenuException : " + menuException.getMessage());
        fileLogger.debug(menuException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Void> orderException(final OrderException orderException) {
        logger.warn("MenuException : " + orderException.getMessage());
        fileLogger.debug(orderException.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(final Exception exception) {
        logger.warn("Exception : " + exception.getMessage());
        fileLogger.debug(exception.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
