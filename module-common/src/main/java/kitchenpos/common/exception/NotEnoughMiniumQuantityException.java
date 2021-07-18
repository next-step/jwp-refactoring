package kitchenpos.common.exception;

public class NotEnoughMiniumQuantityException extends RuntimeException {

    public NotEnoughMiniumQuantityException(long min) {
        super(String.format("최소 수량은 %d개 이상이어야 합니다.", min));
    }
}
