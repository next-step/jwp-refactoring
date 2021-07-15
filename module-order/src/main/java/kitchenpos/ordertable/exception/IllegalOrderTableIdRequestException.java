package kitchenpos.ordertable.exception;

public class IllegalOrderTableIdRequestException extends RuntimeException {
    public IllegalOrderTableIdRequestException() {
        super("요청한 주문 테이블 중 없는 주문테이블이 있습니다");
    }
}
