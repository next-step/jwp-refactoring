package kitchenpos.common;

public enum ErrorMessage {
    EMPTY_TABLE("비어있는 테이블입니다."),
    
    EXIST_GROUP_TABLE("단체로 지정된 테이블입니다."),

    NOT_CHANGE_COMPLETION("완료된 상태는 변경할 수 없습니다."),
    NOT_COMPLETION_STATUS("식사가 완료된 상태가 아닙니다."),
    NOT_EMPTY_TABLE("비어 있지 않은 테이블입니다."),

    NOT_EXIST_COMMON("존재하지 않습니다."),
    NOT_EXIST_MENU_GROUP("메뉴 그룹이 존재하지 않습니다."),
    NOT_EXIST_PRODUCT("제품이 존재하지 않습니다."),
    NOT_EXIST_ORDER_TABLE("주문 테이블이 존재하지 않습니다."),


    INVALID_GUEST_NUMBER("잘못된 고객 수입니다."),
    INVALID_MENU_NUMBER("올바르지 않은 메뉴 갯수입니다."),
    INVALID_PRICE("올바르지 않은 가격입니다."),
    INVALID_QUANTITY("올바르지 않은 수량입니다."),
    INVALID_TABLE_NUMBER("잘못된 테이블 갯수입니다. 2개 이상 묶을 수 있습니다.");

    String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
