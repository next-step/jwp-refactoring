package kitchenpos.menu.exceptions;

public enum MenuErrorCode {
    PRICE_NOT_NULL_AND_ZERO("가격이 비어있거나, 0원 미만일수 없습니다"),
    MENU_PRICE_NOT_OVER_SUM_PRICE("메뉴의 가격이 전체 메뉴 상품의 합보다 클 수 없습니다.");

    private final String message;

    MenuErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
