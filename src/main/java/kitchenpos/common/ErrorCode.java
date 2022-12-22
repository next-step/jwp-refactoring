package kitchenpos.common;

public enum ErrorCode {

    NOT_FOUND_BY_ID("[ERROR] 해당 id에 해당하는 엔티티는 없습니다. "),
    NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP("[ERROR] 주문 테이블이 있어 테이블 그룹에 추가할 수 없습니다."),

    CANNOT_BE_CHANGED_ORDER_STATUS("[ERROR] 변경할 수 없는 주문상태 입니다."),
    CANNOT_CHANGE_NUMBER_OF_GUESTS("[ERROR] 비어있는 테이블은 손님의 숫자를 변경할 수 없습니다."),
    ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP("[ERROR] 이미 해당 주문테이블은 테이블그룹에 속해 있습니다."),
    ORDER_TABLES_CANNOT_BE_EMPTY("[ERROR] 주문 테이블은 비어있을 수 없습니다."),
    MUST_BE_GREATER_THAN_MINIMUM_SIZE("[ERROR] 주문 테이블은 최소 2개 이상이어야 합니다."),
    MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE("[ERROR] 주문 메뉴는 최소 2개 이상이어야 합니다."),
    ORDER_LINE_ITEMS_CANNOT_BE_EMPTY("[ERROR] 주문 상품은 비어있을 수 없습니다."),

    INVALID_QUANTITY("[ERROR] 유효하지 않은 수량입니다."),
    INVALID_NUMBER_OF_GUESTS("[ERROR] 손님 숫자는 음수가 될 수 없습니다."),
    INVALID_NUMBER_OF_ORDER_TABLES("[ERROR] 테이블 그룹핑을 위한 주문테이블은 최소 2개 이상이 되어야 합니다."),
    INVALID_FORMAT_ORDER("[ERROR] 유효하지 않은 주문입니다."),
    INVALID_FORMAT_MENU("[ERROR] 요청한 메뉴는 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_NAME("[ERROR] 요청한 메뉴명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP_NAME("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_QUANTITY("[ERROR] 메뉴수량은 음수가 될 수 없습니다."),
    INVALID_FORMAT_PRODUCT("[ERROR] 요청한 상품은 잘못된 형식입니다."),
    INVALID_FORMAT_PRODUCT_NAME("[ERROR] 요청한 상품명은 잘못된 형식입니다."),
    INVALID_FORMAT_PRICE("[ERROR] 요청한 금액은 잘못된 형식입니다."),
    INVALID_FORMAT_PRICE_IS_NEGATIVE("[ERROR] 요청한 금액은 음수가 될 수 없습니다.."),
    INVALID_ADD_MENU_PRICE("[ERROR] 메뉴상품 추가 시 메뉴상품의 총 금액의 합보다 메뉴의 가격이 클 수 없습니다."),;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
