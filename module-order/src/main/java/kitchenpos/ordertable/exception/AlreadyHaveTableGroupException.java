package kitchenpos.ordertable.exception;

public class AlreadyHaveTableGroupException extends RuntimeException {
    public AlreadyHaveTableGroupException() {
        super("이미 테이블 그룹을 가지고 있는 주문 테이블입니다");
    }
}
