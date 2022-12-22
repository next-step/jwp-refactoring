package kitchenpos.table.message;

public enum NumberOfGuestsMessage {
    CREATE_ERROR_GUESTS_MUST_BE_NOT_NULL("손님명수가 주어지지 않았습니다."),
    CREATE_ERROR_GUESTS_MUST_BE_MORE_THAN_ZERO("손님명수는 0명 이상이어야 합니다."),
    ;

    private final String message;

    NumberOfGuestsMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
