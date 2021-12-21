package kitchenpos.common.exception;

public enum Message {

    PRODUCT_NAME_IS_NOT_EMPTY("상품 이름은 빈 값일 수 없습니다."),
    AMOUNT_PRICE_IS_NOT_EMPTY("금액은 빈 값일 수 없습니다."),
    AMOUNT_IS_NOT_LESS_THAN_ZERO("금액은 0보다 작을 수 없습니다.")

    ;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String format(Message message, Object... arg) {
        return String.format(message.getMessage(), arg);
    }
}
