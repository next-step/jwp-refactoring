package kitchenpos.common;

public enum Message {

    ERROR_MENUGROUP_NAME_REQUIRED("반드시 메뉴그룹의 이름을 입력해야 합니다."),
    ERROR_PRODUCT_NAME_REQUIRED("반드시 상품의 이름을 입력해야 합니다."),
    ERROR_PRODUCT_PRICE_REQUIRED("반드시 상품의 가격을 입력해야 합니다."),
    ERROR_PRODUCT_PRICE_SHOULD_BE_OVER_THAN_ZERO("상품의 가격은 0원 이상이어야 합니다."),
    ERROR_MENU_PRICE_REQUIRED("반드시 메뉴의 가격을 입력해야 합니다."),
    ERROR_MENU_PRICE_SHOULD_BE_OVER_THAN_ZERO("메뉴의 가격은 0원 이상이어야 합니다."),
    ERROR_MENUGROUP_NOT_FOUND("등록되지 않은 메뉴 그룹입니다."),
    ERROR_PRODUCT_NOT_FOUND("등록되지 않은 상품이 있습니다."),
    ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL("메뉴의 가격은 메뉴 상품들의 가격합보다 클 수 없습니다."),
    ERROR_ORDER_TABLE_CANNOT_BE_CLEANED_WHEN_GROUPED("단체지정된 테이블은 비울 수 없습니다."),
    ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO("테이블의 손님수는 0 이상이어야 합니다."),
    ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY("비어있는 테이블의 손님수는 변경할 수 없습니다.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String showText() {
        return message;
    }

}
