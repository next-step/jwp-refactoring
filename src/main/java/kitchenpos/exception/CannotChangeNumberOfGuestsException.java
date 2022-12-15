package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotChangeNumberOfGuestsException extends RuntimeException{

	private static final String MESSAGE = "손님의 수를 변경할 수 없는 테이블입니다";

	public CannotChangeNumberOfGuestsException() {
		super(MESSAGE);
	}
}
