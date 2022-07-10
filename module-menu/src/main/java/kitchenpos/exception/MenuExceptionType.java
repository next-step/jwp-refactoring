package kitchenpos.exception;

public enum MenuExceptionType {
    MENU_GROUP_NOT_FOUND("메뉴 그룹이 존재 하지 않습니다."),
    MIN_PRICE("메뉴 금액은 0보다 커야 합니다."),
    EXCEED_PRICE("메뉴 금액은 상품 전체 가격 보다 작아야 합니다.");

    private final String message;

    MenuExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
