package kitchenpos.common;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {

	@ExceptionHandler(
		{
			IllegalArgumentException.class,
			EntityNotFoundException.class
		})
	public ResponseEntity<?> handleBadRequestException(Exception exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}
}
