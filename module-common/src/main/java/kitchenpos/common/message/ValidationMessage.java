package kitchenpos.common.message;

public final class ValidationMessage {
    public static final String NOT_EMPTY = "빈 값이나 공백이 들어올 수 없습니다.";
    public static final String NOT_NULL = "빈 값이이 들어올 수 없습니다.";

    public static final String POSITIVE = "양수 값만 올 수 있습니다.";
    public static final String POSITIVE_OR_ZERO = "0 이상의 값만 올 수 있습니다.";

    public static final String MIN_SIZE_IS_ONE = "1 이상의 크기여야 합니다.";
    public static final String MIN_SIZE_IS_TWO = "2 이상의 크기여야 합니다.";

    private ValidationMessage() {
    }
}
