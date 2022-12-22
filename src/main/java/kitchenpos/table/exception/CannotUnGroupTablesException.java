package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotUnGroupTablesException extends RuntimeException {

	private static final String MESSAGE = "완료되지 않은 주문이 있습니다";

	public CannotUnGroupTablesException() {
		super(MESSAGE);
	}
}
