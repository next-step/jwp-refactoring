package kitchenpos.product.exception;

public enum ProductPriceExceptionType {

    LESS_THEN_ZERO_PRICE("가격은 0보다 작을수 없습니다");
    public String message;

    ProductPriceExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
