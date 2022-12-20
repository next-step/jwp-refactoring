package kitchenpos.table.exception;

public class CannotUngroupException extends RuntimeException {

    public CannotUngroupException() {
        super("단체 지정을 해지할 수 없습니다.");
    }
}
