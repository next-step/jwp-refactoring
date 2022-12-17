package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotStartOrderException extends RuntimeException {

	private CannotStartOrderException(String message) {
		super(message);
	}

	public CannotStartOrderException(TYPE type) {
		this(type.message);
	}

	public enum TYPE {
		NO_ORDER_ITEMS("주문할 메뉴가 없습니다"),
		ORDER_TABLE_NOT_EMPTY("주문 테이블이 비어있지 않습니다")
		,
		;

		public final String message;

		TYPE(String message) {
			this.message = message;
		}
	}
}
