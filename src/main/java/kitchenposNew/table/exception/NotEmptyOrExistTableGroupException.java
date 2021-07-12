package kitchenposNew.table.exception;

public class NotEmptyOrExistTableGroupException extends RuntimeException {
    public NotEmptyOrExistTableGroupException() {
        super("단체 테이블은 빈 테이블이고 다른 단체테이블에 속하면 안됩니다.");
    }
}
