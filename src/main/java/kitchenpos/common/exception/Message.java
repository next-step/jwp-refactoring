package kitchenpos.common.exception;

public enum Message {

    PRODUCT_NAME_IS_NOT_EMPTY("상품 이름은 빈 값일 수 없습니다."),
    AMOUNT_PRICE_IS_NOT_EMPTY("금액은 빈 값일 수 없습니다."),
    AMOUNT_IS_NOT_LESS_THAN_ZERO("금액은 0보다 작을 수 없습니다."),
    MENU_AMOUNT_IS_TOO_LAGE("입력한 금액이 음식 총 합보다 큽니다."),
    ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP("빈 테이블이 아니거나 이미 단체지정이 된 테이블 입니다."),
    ORDER_TABLES_IS_SMALL_THAN_MIN_TABLE_SIZE("주문 테이블이 최소크기보다 크거나 같아야합니다."),
    ORDER_STATUS_IS_NOT_COMPLETION("계산이 완료되어 변경할 수 없습니다.")
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
