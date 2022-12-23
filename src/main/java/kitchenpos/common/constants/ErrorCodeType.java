package kitchenpos.common.constants;

public enum ErrorCodeType {

    MENU_PRICE_NOT_OVER_SUM_PRICE("메뉴의 가격이 전체 메뉴 상품의 합보다 클 수 없습니다."),
    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다"),
    COOKING_MEAL_NOT_UNGROUP("조리나 식사 상태면 상태를 변경 할 수 없습니다"),
    ORDER_LINE_ITEM_MENU_SIZE_DIFFERENT("주문항목과 메뉴의 갯수는 같아야합니다"),
    MENU_GROUP_NOT_FOUND("메뉴 그룹 을 찾을 수 없습니다"),
    TABLE_GROUP_NOT_FOUND("단체 지정을 찾을 수 없습니다."),
    MATCH_GROUP_PRESENT("단체지정석이 존재합니다"),
    TABLE_GROUP_NOT_NULL("단체 지정은 비어있으면 안됩니다"),
    ORDER_TABLES_SIZE_IS_EMPTY_AND_MIN_SIZE("주문 테이블은 비어있거나, 한개 이하이면 안됩니다."),
    ORDER_STATUS_NOT_COMPLETION("주문 상태가 이미 완료 상태입니다."),
    ORDER_LINE_ITEM_REQUEST("주문항목은 비어있을 수 없습니다"),
    PRICE_NOT_NULL_AND_ZERO("가격이 비어있거나, 0원 미만일수 없습니다"),
    NOT_FOUND_ORDER_TABLE("주문테이블에 주문이 없습니다"),
    ORDER_TABLE_MIN_SIZE_ERROR("주문 테이블은 최소 2개 이상이여야 합니다"),
    NOT_MATCH_ORDER_TABLE("매치 되는 주문테이블이 없습니다"),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다"),
    MATCH_NOT_MENU("메누와 메칭 되는것을 찾을 수 없습니다"),
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다"),
    ORDER_TABLE_NOT_FOUND("주문 테이블을 찾을 수 없습니다"),
    GUEST_NOT_NULL_AND_ZERO("손님은 0명 미만이거나 비어 있을 수 없습니다");

    private final String message;

    ErrorCodeType(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    }
