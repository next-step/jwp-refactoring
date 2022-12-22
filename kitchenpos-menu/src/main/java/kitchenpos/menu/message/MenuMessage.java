package kitchenpos.menu.message;

public enum MenuMessage {
    CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL("메뉴 그룹은 반드시 포함되어야 합니다."),
    ADD_PRODUCT_ERROR_IN_VALID_PRICE("메뉴 가격은 등록 된 상품의 요금(가격 * 수량)이 합산된 금액보다 작거나 같아야 합니다."),
    ;

    private final String message;

    MenuMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
