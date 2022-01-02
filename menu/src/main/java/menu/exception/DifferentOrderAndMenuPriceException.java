package menu.exception;

import common.config.exception.PriceException;

public class DifferentOrderAndMenuPriceException extends PriceException {
    public DifferentOrderAndMenuPriceException() {
        super("메뉴 가격과 상품 가격 합이 같지 않습니다.");
    }
}
