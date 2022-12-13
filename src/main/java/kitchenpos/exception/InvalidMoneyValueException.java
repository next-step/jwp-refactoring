package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMoneyValueException extends RuntimeException {

	private static final String MESSAGE = "금액은 0보다 작을 수 없습니다.";

	public InvalidMoneyValueException() {
		super(MESSAGE);
	}
}
