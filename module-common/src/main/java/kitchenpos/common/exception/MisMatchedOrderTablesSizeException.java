package kitchenpos.common.exception;

public class MisMatchedOrderTablesSizeException extends RuntimeException {
    public MisMatchedOrderTablesSizeException() {
        super("입력된 항목과 조회결과가 일치하지 않습니다.");
    }

    public MisMatchedOrderTablesSizeException(String message) {
        super(message);
    }
}
