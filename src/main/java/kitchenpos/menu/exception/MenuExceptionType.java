package kitchenpos.menu.exception;

public enum MenuExceptionType {
    EXCEED_MENU_PRODUCT_PRICE("메뉴가격은 상품가격을 초과할 수 없습니다");
    public String message;

    MenuExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
