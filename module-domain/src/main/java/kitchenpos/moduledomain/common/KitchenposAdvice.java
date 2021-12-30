package kitchenpos.moduledomain.common;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.persistence.EntityNotFoundException;
import kitchenpos.exception.ErrorResponse;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KitchenposAdvice {

    @ExceptionHandler(value = {EntityNotFoundException.class, DataIntegrityViolationException.class,
        NoResultDataException.class})
    public ResponseEntity<ErrorResponse> EntityNotFoundException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), INTERNAL_SERVER_ERROR);
    }
}
