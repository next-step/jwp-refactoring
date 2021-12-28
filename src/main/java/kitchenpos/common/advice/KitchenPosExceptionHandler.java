package kitchenpos.common.advice;

import kitchenpos.common.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * packageName : kitchenpos.common.advice
 * fileName : KichenPosExceptionHandler
 * author : haedoang
 * date : 2021/12/25
 * description :
 *  @see <a href="https://cheese10yun.github.io/spring-guide-exception/#null">Exception 전략</a>
 *  @see <a href="https://github.com/cheese10yun/spring-guide/blob/master/docs/exception-guide.md">Exception 전략 참고코드</a>
 */
@RestControllerAdvice
public class KitchenPosExceptionHandler {

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
