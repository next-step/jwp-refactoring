package kitchenpos.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kitchenpos.dto.ErrorResponse;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadRequestException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
