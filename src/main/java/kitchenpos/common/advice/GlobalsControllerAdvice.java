package kitchenpos.common.advice;

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
}
