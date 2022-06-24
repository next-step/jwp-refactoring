package kitchenpos.tablegroup.exception;

public class NotFoundTableGroupException extends RuntimeException {

    public NotFoundTableGroupException() {
        super("단체 지정을 찾을 수 없습니다.");
    }
}
