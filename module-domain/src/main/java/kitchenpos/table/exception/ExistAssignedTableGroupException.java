package kitchenpos.table.exception;

public class ExistAssignedTableGroupException extends RuntimeException {

    public ExistAssignedTableGroupException() {
        super("이미 다른 단체로 지정된 주문테이블이 존재합니다.");
    }
}
