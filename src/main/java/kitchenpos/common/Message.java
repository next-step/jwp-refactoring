package kitchenpos.common;

public enum Message {

    ERROR_MENUGROUP_NAME_REQUIRED("반드시 메뉴그룹의 이름을 입력해야 합니다."),
    ERROR_PRODUCT_NAME_REQUIRED("반드시 상품의 이름을 입력해야 합니다."),
    ERROR_PRODUCT_PRICE_REQUIRED("반드시 상품의 가격을 입력해야 합니다."),
    ERROR_PRODUCT_PRICE_SHOULD_BE_OVER_THAN_ZERO("상품의 가격은 0원 이상이어야 합니다.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String showText() {
        return message;
    }
}
