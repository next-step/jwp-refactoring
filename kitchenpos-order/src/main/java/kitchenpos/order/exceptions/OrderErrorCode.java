package kitchenpos.order.exceptions;

public enum OrderErrorCode {
    ORDER_LINE_ITEM_REQUEST("주문항목은 비어있을 수 없습니다"),
    ORDER_STATUS_NOT_COMPLETION("주문 상태가 이미 완료 상태입니다."),
    TABLE_GROUP_NOT_NULL("단체 지정은 비어있으면 안됩니다"),
    MATCH_NOT_MENU("메누와 메칭 되는것을 찾을 수 없습니다"),
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다"),
    COOKING_MEAL_NOT_UNGROUP("조리나 식사 상태면 상태를 변경 할 수 없습니다"),
    GUEST_NOT_NULL_AND_ZERO("손님은 0명 미만이거나 비어 있을 수 없습니다"),

    MENU_PRICE_NOT_OVER_SUM_PRICE("메뉴의 가격이 전체 메뉴 상품의 합보다 클 수 없습니다.");

    private final String message;

    OrderErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

