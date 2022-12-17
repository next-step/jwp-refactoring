package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotCreateGroupTableException extends RuntimeException {

	private CannotCreateGroupTableException(String message) {
		super(message);
	}

	public CannotCreateGroupTableException(TYPE type) {
		this(type.message);
	}

	public enum TYPE {
		HAS_NO_ORDER_TABLE("그룹핑할 주문 테이블이 없습니다"),
		INVALID_TABLE_COUNT("테이블 갯수가 2개 이상이 아닙니다"),
		NOT_EMPTY_ORDER_TABLE("테이블이 비어있지 않습니다"),
		HAS_GROUP_TABLE("이미 테이블 그룹에 주문 테이블이 존재합니다"),
		;

		public final String message;

		TYPE(String message) {
			this.message = message;
		}
	}
}
