package kitchenpos.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotChangeOrderStatusException extends RuntimeException {

	private static final String MESSAGE = "완료된 주문은 상태를 변경할 수 없습니다";

	public CannotChangeOrderStatusException() {
		super(MESSAGE);
	}
}
