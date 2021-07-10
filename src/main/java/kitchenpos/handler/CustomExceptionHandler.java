package kitchenpos.handler;

import kitchenpos.handler.exception.NotChangeNumberOfGuestsException;
import kitchenpos.handler.exception.NotChangeStatusException;
import kitchenpos.handler.exception.NotCreateMenuException;
import kitchenpos.handler.exception.NotCreateOrderException;
import kitchenpos.handler.exception.NotCreateTableGroupException;
import kitchenpos.handler.exception.NotFoundEntityException;
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

    @ExceptionHandler(NotChangeStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleChangeEmptyException(NotChangeStatusException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotChangeNumberOfGuestsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotChangeNumberOfGuestsException(NotChangeNumberOfGuestsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundEntityException(NotFoundEntityException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotCreateMenuException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotCreateMenuException(NotCreateMenuException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotCreateTableGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotCreateTableGroupException(NotCreateTableGroupException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotCreateOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotCreateOrderException(NotCreateOrderException e) {
        return e.getMessage();
    }
}
