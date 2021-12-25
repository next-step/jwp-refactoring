package kitchenpos.exception;

public class CannotChangeNumberOfGuestsException extends RuntimeException {
    public CannotChangeNumberOfGuestsException() {
        super("빈 테이블의 손님 숫자를 지정할 수 없습니다");
    }
}
