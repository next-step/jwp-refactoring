package kitchenpos.common;

import kitchenpos.table.exception.NotChangeEmptyException;
import kitchenpos.table.exception.NotChangeNumberOfGuestsException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotChangeEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleChangeEmptyException(NotChangeEmptyException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundOrderTableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundOrderTableException(NotFoundOrderTableException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotChangeNumberOfGuestsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotChangeNumberOfGuestsException(NotChangeNumberOfGuestsException e) {
        return e.getMessage();
    }
}
