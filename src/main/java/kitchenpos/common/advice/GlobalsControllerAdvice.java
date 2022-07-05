package kitchenpos.common.advice;

import kitchenpos.menu.exception.MenuException;
import kitchenpos.product.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalsControllerAdvice {
    public final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(ProductException.class)
    public void productException(final ProductException productException) {
        fileLogger.debug(productException.getMessage());
    }

    @ExceptionHandler(MenuException.class)
    public void productException(final MenuException menuException) {
        fileLogger.debug(menuException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void productException(final Exception exception) {
        fileLogger.debug(exception.getMessage());
    }
}
