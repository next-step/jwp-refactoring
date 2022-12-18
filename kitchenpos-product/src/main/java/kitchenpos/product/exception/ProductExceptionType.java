package kitchenpos.product.exception;

public enum ProductExceptionType {

    NONE_EXISTS_PRODUCT("상품이 존재하지 않습니다");
    public String message;

    ProductExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
