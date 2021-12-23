package kitchenpos.exception;

public class NoTableGroupException extends RuntimeException {
    public NoTableGroupException() {
        super("테이블 그룹이 없습닌다");
    }
}
