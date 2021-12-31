package kitchenpos.menu.exception;

public class NotFoundMenuProductsException extends IllegalArgumentException {

	public static final String MESSAGE = "메뉴를 구성하는 상품 목록이 있어야 합니다.";

	public NotFoundMenuProductsException() {
		super(MESSAGE);
	}
}
