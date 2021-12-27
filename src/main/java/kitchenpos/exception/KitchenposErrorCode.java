package kitchenpos.exception;

public enum KitchenposErrorCode {
    TABLE_IS_EMPTY("주문 테이블이 비어있습니다."),
    TABLE_IS_IN_GROUP("주문 테이블이 그룹에 포함되어 있습니다."),
    INVALID_NUMBER_OF_GUESTS("0 이상의 고객수만 입력 가능합니다."),
    INVALID_PRICE("0 이상의 가격만 입력 가능합니다."),
    INVALID_MENU_PRICE("각 상품 가격의 합보다 많은 가격입니다."),
    INVALID_TABLE_GROUP_REQUEST("테이블 그룹을 생성하기 위해 2개 이상의 테이블이 필요합니다.");

    private final String message;

    KitchenposErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
