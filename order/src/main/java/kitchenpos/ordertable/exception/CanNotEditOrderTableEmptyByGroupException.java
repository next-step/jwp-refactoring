package kitchenpos.ordertable.exception;

public class CanNotEditOrderTableEmptyByGroupException extends IllegalArgumentException {

	public static final String MESSAGE = "주문 테이블이 단체 지정된 경우 수정할 수 없습니다.";

	public CanNotEditOrderTableEmptyByGroupException() {
		super(MESSAGE);
	}
}
