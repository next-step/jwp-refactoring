package kitchenpos.product.exception;

import kitchenpos.common.exception.PriceNotAcceptableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(PriceNotAcceptableException.class)
    public ResponseEntity handlePriceNotAcceptableException(
        PriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }
}
