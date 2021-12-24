package kitchenpos.product.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {
    
    @ExceptionHandler(ProductPriceNotAcceptableException.class)
    public ResponseEntity handleProductPriceNotAcceptableException(
        ProductPriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }
}
