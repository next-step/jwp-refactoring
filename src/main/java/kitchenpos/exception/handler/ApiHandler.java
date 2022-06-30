package kitchenpos.exception.handler;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.dto.ExceptionDTO;
import kitchenpos.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiHandler {

    @ExceptionHandler
    public ResponseEntity IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity CustomExceptionHandler(CustomException e) {
        ExceptionDTO exceptionDTO = e.getExceptionDTO();
        String errorMessage = messageAppender(
            Arrays.asList(exceptionDTO.getClassification(), " : ", e.getMessage()));
        return ResponseEntity.badRequest()
            .body(errorMessage);
    }

    private String messageAppender(List<String> sources) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String source : sources) {
            stringBuilder.append(source);
        }
        return stringBuilder.toString();
    }


}
