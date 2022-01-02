package menu.exception;

public class DifferentOrderAndMenuPriceException extends RuntimeException {
    public DifferentOrderAndMenuPriceException() {
        super("메뉴 가격과 상품 가격 합이 같지 않습니다.");
    }
}
