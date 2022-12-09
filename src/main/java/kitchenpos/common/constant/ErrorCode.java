package kitchenpos.common.constant;

public enum ErrorCode {

    이름은_비어_있을_수_없음("[ERROR] 이름은 비어있을 수 없습니다."),
    방문한_손님_수는_0보다_작을_수_없음("[ERROR] 방문한 손님 수는 0보다 작을 수 없습니다."),
    가격은_비어있을_수_없음("[ERROR] 가격은 비어있을 수 없습니다."),
    가격은_0보다_작을_수_없음("[ERROR] 가격은 0보다 작을 수 없습니다."),
    수량은_0보다_작을_수_없음("[ERROR] 수량은 0보다 작을 수 없습니다."),

    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
