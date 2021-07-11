package kitchenpos.ordertable.exception;

public class IllegalOrderTableEmptyChangeException extends RuntimeException {
    public IllegalOrderTableEmptyChangeException() {
        super("주문 테이블이 1개 이하인 테이블 그룹을 생성하려고 합니다.");
    }
}
