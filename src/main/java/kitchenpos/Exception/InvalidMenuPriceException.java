package kitchenpos.Exception;

import kitchenpos.common.Price;

public class InvalidMenuPriceException extends RuntimeException {
    private static final String message = "메뉴 상품 가격의 총 합(%s)은 메뉴 가격(%s)보다 클 수 없습니다.";

    public InvalidMenuPriceException(Price menuPrice, Price sumPrice) {
        super(String.format(message, sumPrice.toString(), menuPrice.toString()));
    }
}
