package kitchenpos.common.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorApiResult handleError(EntityNotFoundException e) {
        return ErrorApiResult.of(HttpStatus.NOT_FOUND.value(), e.getMessage(), e);
    }


    @ExceptionHandler(PriceException.class)
    public ErrorApiResult handlePriceError(PriceException e) {
        return ErrorApiResult.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
    }

    @ExceptionHandler(NotValidRequestException.class)
    public ErrorApiResult handleValidError(NotValidRequestException e) {
        return ErrorApiResult.of(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e);
    }

}
