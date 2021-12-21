package kitchenpos.common;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.persistence.EntityNotFoundException;
import kitchenpos.common.exception.ErrorResponse;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.common.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KitchenposAdvice {

    @ExceptionHandler(value = {EntityNotFoundException.class, DataIntegrityViolationException.class, NoResultDataException.class})
    public ResponseEntity<ErrorResponse> EntityNotFoundException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<ErrorResponse> serviceException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, ArithmeticException.class})
    public ResponseEntity<ErrorResponse> standardException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), BAD_REQUEST);
    }
}
