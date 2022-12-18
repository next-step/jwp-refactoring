package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberOfGuestsException extends RuntimeException {

	private static final String MESSAGE = "유효하지 않은 손님의 수 입니다.";

	public InvalidNumberOfGuestsException() {
		super(MESSAGE);
	}
}
