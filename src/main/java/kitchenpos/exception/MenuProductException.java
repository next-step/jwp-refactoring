package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MenuProductException extends RuntimeException {
    public static final String MENU_PRICE_MORE_EXPENSIVE_PRODUCTS_MSG = "메뉴의 가격은 상품들의 가격들의 합보다 크면 안됩니다.";

    public MenuProductException() {
    }

    public MenuProductException(String msg) {
        super(msg);
    }
}
