package kitchenpos.common.ui;

import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionController {

	@ExceptionHandler
	public ResponseEntity internalError(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler({EntityNotFoundException.class, IllegalArgumentException.class})
	public ResponseEntity badRequest(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
