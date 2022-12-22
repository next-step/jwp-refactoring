package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotChangeEmptyOrderTable extends RuntimeException {

	public CannotChangeEmptyOrderTable(Type type) {
		super(type.message);
	}

	public enum Type {
		IN_TABLE_GROUP("테이블이 테이블 그룹에 존재합니다"),
		NOT_COMPLETED_ORDER("완료되지 않은 주문이 있습니다");

		private final String message;

		Type(String message) {
			this.message = message;
		}
	}
}
