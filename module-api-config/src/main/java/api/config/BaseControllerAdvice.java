package api.config;

import common.application.NotFoundException;
import common.application.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseControllerAdvice {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity validationException(ValidationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(e));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity validationException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(e));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String unhandledException(Exception e) {
		return "internal server error";
	}
}
