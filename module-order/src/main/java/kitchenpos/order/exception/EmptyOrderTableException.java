package kitchenpos.order.exception;

public class EmptyOrderTableException extends RuntimeException{
    public EmptyOrderTableException() {
        super("비어있는 주문테이블에서 주문을 생성하려고 합니다");
    }
}
