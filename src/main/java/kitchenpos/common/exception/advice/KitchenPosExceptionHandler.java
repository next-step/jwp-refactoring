package kitchenpos.common.exception.advice;

import kitchenpos.common.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName : kitchenpos.common.exception.advice
 * fileName : KichenPosExceptionHandler
 * author : haedoang
 * date : 2021/12/25
 * description :
 */
@RestControllerAdvice
public class KitchenPosExceptionHandler {

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
