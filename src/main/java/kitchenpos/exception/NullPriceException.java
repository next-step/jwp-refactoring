package kitchenpos.exception;

public class NullPriceException extends RuntimeException {
    public NullPriceException() {
        super("가격이 없는 상품을 생성할 수 없습니다");
    }
}
