package common.exception;

public class NegativeQuantityException extends CustomException {
    public static final String NEGATIVE_QUANTITY_MESSAGE = "수량은 없을 수 없으며 최송 수량 이상이어야 합니다.";

    public NegativeQuantityException() {
        super(NEGATIVE_QUANTITY_MESSAGE);
    }
}
