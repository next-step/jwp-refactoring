package kitchenpos.common.constant;

public enum ErrorCode {

    NAME_NOT_EMPTY("이름은 빈 값을 허용하지 않습니다."),
    NUMBER_OF_GUESTS_LESS_THAN_ZERO("방문한 손님은 0명보다 작을 수 없습니다."),
    PRICE_NOT_EMPTY("가격은 빈 값을 허용하지 않습니다."),
    PRICE_LESS_THAN_ZERO("가격은 0원보다 작을 수 없습니다."),
    QUANTITY_LESS_THAN_ZERO("수량은 0단위보다 작을 수 없습니다."),
    MENU_PRICE_MORE_THAN_TOTAL_PRICE("메뉴의 가격은 전체 메뉴 상품 가격보다 크지 않습니다."),
    MENU_GROUP_NOT_EMPTY("메뉴 그룹은 빈 값을 허용하지 않습니다."),
    MENU_PRODUCT_NOT_EMPTY("메뉴 상품은 빈 값을 허용하지 않습니다."),
    HAS_TABLE_GROUP("단체 그룹이 지정되어 있습니다."),
    ORDER_TABLE_NOT_EMPTY("주문 테이블이 비어 있습니다."),
    NOT_COMPLETE_ORDER("완료되지 않은 주문입니다."),
    ALREADY_COMPLETE_ORDER("이미 완료된 주문입니다."),
    ORDER_TABLE_MIN_SIZE("주문 테이블 2개 이상이여야 합니다."),
    ORDER_LINE_ITEM_NOT_EMPTY("주문 항목은 빈 값을 허용하지 않습니다.");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
