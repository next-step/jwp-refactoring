package kitchenpos.tablegroup.exception;

public class CanNotUngroupByOrderStatusException extends IllegalArgumentException {

	public static final String MESSAGE = "주문 테이블이 조리 혹은 식사 상태인 경우 단체 지정을 해지할 수 없습니다.";

	public CanNotUngroupByOrderStatusException() {
		super(MESSAGE);
	}
}
