package kitchenpos.common.exception;

public class NonEmptyOrderTableNotFoundException extends RuntimeException {
    public NonEmptyOrderTableNotFoundException() {
        super("비어있지 않은 테이블 대상이 존재하지 않습니다.");
    }

    public NonEmptyOrderTableNotFoundException(String message) {
        super(message);
    }
}
