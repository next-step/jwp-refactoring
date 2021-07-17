package kitchenpos.product.exception;

public class MenuPriceExceedException extends RuntimeException {

    public MenuPriceExceedException() {
        super("메뉴의 가격은 메뉴상품들 가격의 합을 초과할 수 없습니다.");
    }
}
