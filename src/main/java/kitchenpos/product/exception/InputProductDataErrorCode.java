package kitchenpos.product.exception;

public enum InputProductDataErrorCode {

    IT_CAN_NOT_INPUT_PRICE_LESS_THAN_ZERO("[ERROR]0보다 작은 가격을 입력할 수 없습니다");

    private String errorMessage;

    InputProductDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}
