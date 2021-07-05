package kitchenpos.order.exception;

public class NoSuchMemuListException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String NO_ORDER = "유효하지 않은 Menu가 존재합니다. 입력값을 확인하세요";

    public NoSuchMemuListException() {
        super(NO_ORDER);
    }

    public NoSuchMemuListException(String message) {
        super(message);
    }
}
