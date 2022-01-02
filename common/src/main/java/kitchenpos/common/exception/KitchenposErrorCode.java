package kitchenpos.common.exception;

public enum KitchenposErrorCode {
    TABLE_IS_EMPTY("주문 테이블이 비어있습니다."),
    TABLE_IS_IN_GROUP("주문 테이블이 그룹에 포함되어 있습니다."),
    INVALID_NUMBER_OF_GUESTS("0 이상의 고객수만 입력 가능합니다."),
    INVALID_PRICE("0 이상의 가격만 입력 가능합니다."),
    INVALID_MENU_PRICE("각 상품 가격의 합보다 많은 가격입니다."),
    INVALID_TABLE_GROUP_REQUEST("테이블 그룹을 생성하기 위해 2개 이상의 테이블이 필요합니다."),
    CONTAINS_USED_TABLE("사용중인 테이블이 있습니다."),
    EMPTY_ORDER_LINE_ITEMS("주문 항목이 비어있습니다."),
    INVALID_ORDER_LINE_ITEM_SIZE("주문 항목의 개수가 다릅니다."),
    CANNOT_UPDATE_COMPLETED_ORDER("완료된 주문의 상태를 바꿀 수 없습니다."),
    INVALID_TABLE_SIZE("주문 테이블의 개수가 다릅니다.");

    private final String message;

    KitchenposErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
