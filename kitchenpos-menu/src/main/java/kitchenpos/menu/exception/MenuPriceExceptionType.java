package kitchenpos.menu.exception;

public enum MenuPriceExceptionType {
    LESS_THEN_ZERO_PRICE("가격은 0보다 작을수 없습니다");
    public String message;

    MenuPriceExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
