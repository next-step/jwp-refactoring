package kitchenpos.advice;


import kitchenpos.advice.exception.MenuGroupBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class KitchenposExceptionHandler {

    @ExceptionHandler(MenuGroupBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(MenuGroupBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
