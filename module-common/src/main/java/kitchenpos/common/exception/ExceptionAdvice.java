package kitchenpos.common.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotExistException.class)
    protected ResponseEntity handleNotExistException(NotExistException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({InvalidGuestNumberException.class, InvalidMenuNumberException.class,
            InvalidTableNumberException.class})
    protected ResponseEntity handleInvalidNumberException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler({InvalidOrderStatusException.class, NotCompletionStatusException.class})
    protected ResponseEntity handleInvalidStatusException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        final List<ObjectError> errors = e.getBindingResult().getAllErrors();
        final Map<String, String> body = errors.stream()
                .collect(Collectors.toMap(
                        field -> ((FieldError) field).getField(),
                        message -> message.getDefaultMessage()
                ));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
