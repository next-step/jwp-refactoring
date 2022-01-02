package kitchenpos.common;

public class WrongValueException extends RuntimeException {
    public static final String WRONG_VALUE_EXCEPTION_STATEMENT = "%s 은(는) 잘못되었습니다.";

    public WrongValueException(String target) {
        super(String.format(WRONG_VALUE_EXCEPTION_STATEMENT, target));
    }
}
