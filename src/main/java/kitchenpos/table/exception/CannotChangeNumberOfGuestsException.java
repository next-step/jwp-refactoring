package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotChangeNumberOfGuestsException extends RuntimeException {

	public CannotChangeNumberOfGuestsException(Type type) {
		super(type.message);
	}

	public enum Type {
		INVALID_NUMBER_OF_GUESTS("유효하지 않은 손님의 수 입니다."),
		NOT_EMPTY("비어있지 않은 테이블입니다"),
		;

		public final String message;

		Type(String message) {
			this.message = message;
		}
	}
}
