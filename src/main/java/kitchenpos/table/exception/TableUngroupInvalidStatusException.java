package kitchenpos.table.exception;

public class TableUngroupInvalidStatusException extends RuntimeException {
    private static final String message = "주문 상태가 모두 완료일때만 단체 지정해제가 가능합니다.";

    public TableUngroupInvalidStatusException() {
        super(message);
    }
}
