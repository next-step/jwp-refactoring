package kitchenpos.common.exception;

import java.util.Objects;

public enum ExceptionType {
    INVALID_NAME("잘못된 이름입니다."),
    CONTAINS_NOT_EXIST_PRODUCT("존재하지 않는 상품이 포함되어 있습니다"),
    INVALID_PRICE("잘못된 금액입니다."),
    IS_NOT_OVER_THAN_MENU_PRICE("메뉴 상품의 가격의 합보다 메뉴 가격이 높을 수 없습니다."),
    NOT_EXIST_ORDER_TABLE("존재하지 않는 주문 테이블입니다."),
    EMPTY_ORDER_LINE_ITEM("주문항목이 비어있습니다."),
    CONTAINS_NOT_EXIST_MENU("존재하지 않는 메뉴가 포함되어 있습니다"),
    NOT_EXIST_ORDER("존재하지 않는 주문입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다."),
    COMPLETION_STATUS_CAN_NOT_CHANGE("주문완료 상태는 변경할 수 없습니다."),
    NOT_EXIST_MENU("존재하지 않는 메뉴입니다."),
    ORDER_TABLE_AT_LEAST_TWO("주문 테이블은 최소 2개 이상이어야 합니다."),
    CONTAINS_NOT_EXIST_ORDER_TABLE("존재하지 않는 주문 테이블이 포함되어 있습니다"),
    NOT_EXIST_TABLE_GROUP("존재하지 않는 테이블 그룹입니다."),
    CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS("요리중, 식사중인 테이블은 변경할 수 없습니다."),
    EMPTY_TABLE("테이블이 비어있습니다."),
    CAN_NOT_LESS_THAN_ZERO_GUESTS("손님이 0명 미만일 수 없습니다."),
    TABLE_IS_GROUPED("테이블이 그룹되어 있습니다."),
    MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE("테이블이 비어있지 않거나 그룹이 있으면 안됩니다.");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Long id) {
        if (Objects.nonNull(id)) {
            return message + " [" + id + "]";
        }

        return message;
    }
}
