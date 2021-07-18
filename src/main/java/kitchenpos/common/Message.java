package kitchenpos.common;

public enum Message {

    ERROR_MENUGROUP_NAME_REQUIRED("반드시 메뉴그룹의 이름을 입력해야 합니다."),
    ERROR_PRODUCT_NAME_REQUIRED("반드시 상품의 이름을 입력해야 합니다."),
    ERROR_PRICE_REQUIRED("반드시 가격을 입력해야 합니다."),
    ERROR_PRICE_SHOULD_BE_OVER_THAN_ZERO("가격은 0원 이상이어야 합니다."),
    ERROR_MENUGROUP_NOT_FOUND("등록되지 않은 메뉴 그룹입니다."),
    ERROR_PRODUCT_NOT_FOUND("등록되지 않은 상품이 있습니다."),
    ERROR_TABLE_GROUP_NOT_FOUND("등록되지 않은 상품이 있습니다."),
    ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL("메뉴의 가격은 메뉴 상품들의 가격합보다 클 수 없습니다."),
    ERROR_ORDER_TABLE_CANNOT_BE_CLEANED_WHEN_GROUPED("단체지정된 테이블은 비울 수 없습니다."),
    ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO("테이블의 손님수는 0 이상이어야 합니다."),
    ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY("비어있는 테이블의 손님수는 변경할 수 없습니다."),
    ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM("주문 항목은 1개 이상이어야 합니다."),
    ERROR_ORDER_SHOULD_HAVE_REGISTERED_TABLE("주문 시, 테이블은 반드시 등록되어 있어야 합니다."),
    ERROR_ORDER_SHOULD_HAVE_NON_EMPTY_TABLE("비어있는 테이블의 주문을 받을 수 없습니다."),
    ERROR_ORDER_SHOULD_HAVE_REGISTERED_MENU("등록된 메뉴만 주문할 수 있습니다."),
    ERROR_ORDER_NOT_FOUND("등록되지 않은 주문입니다."),
    ERROR_ORDER_STATUS_CANNOT_BE_CHANGED_WHEN_COMPLETED("완료 처리된 주문의 상태는 변경할 수 없습니다."),
    ERROR_ORDER_TABLE_NOT_FOUND("등록되지 않은 주문테이블입니다."),
    ERROR_ORDER_TABLE_CANNOT_BE_EMPTY_WHEN_ORDERS_IN_COOKING_OR_MEAL("주문의 상태가 요리중이거나 식사중인 테이블은 비울 수 없습니다."),
    ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES("주문 테이블 목록에는 최소 2개 이상의 테이블이 있어야 합니다."),
    ERROR_TABLES_CANNOT_BE_GROUPED("이미 단체지정 되어있거나, 비어있지 않은 테이블은 단체지정할 수 없습니다."),
    ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED("단체 지정이 되기 위해서는 모든 테이블이 등록되어 있어야 합니다."),
    ERROR_TABLE_GROUP_CANNOT_BE_UNGROUPED_WHEN_ORDERS_NOT_COMPLETED("주문들이 완료되지 않은 상태에서는 단체지정을 해제할 수 없습니다"),
    ERROR_TABLE_GROUP_CANNOT_BE_UNGROUPED_WHEN_TABLES_NOT_EMPTY("주문 테이블이 비어있지 않은 상태에서는 단체지정을 해제할 수 없습니다")
    ;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String showText() {
        return message;
    }

}
