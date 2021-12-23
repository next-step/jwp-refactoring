package kitchenpos.menu.exception;

import java.math.BigDecimal;

public class InvalidMenuPriceException extends IllegalArgumentException {

	public static final String MESSAGE = "메뉴의 가격(%.0f)은 메뉴를 구성하는 상품들의 합산 가격(%.0f)과 같거나 작아야 합니다.";

	public InvalidMenuPriceException(BigDecimal menuPrice, BigDecimal sumMenuProductsPrice) {
		super(String.format(MESSAGE, menuPrice, sumMenuProductsPrice));
	}
}
