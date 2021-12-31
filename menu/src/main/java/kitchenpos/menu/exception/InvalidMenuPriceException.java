package kitchenpos.menu.exception;

import kitchenpos.common.Price;

public class InvalidMenuPriceException extends IllegalArgumentException {

	public static final String MESSAGE = "메뉴의 가격(%.0f)은 메뉴를 구성하는 상품들의 합산 가격(%.0f)과 같거나 작아야 합니다.";

	public InvalidMenuPriceException(Price menuPrice, Price sumMenuProductsPrice) {
		super(String.format(MESSAGE, menuPrice.getPrice(), sumMenuProductsPrice.getPrice()));
	}
}
