package kitchenpos.exception;

public enum CommonConstants {
    INVALID_FORMAT_PRICE ("[ERROR] 테이블 그룹핑을 위한 주문테이블은 최소 2개 이상이 되어야 합니다."),
    INVALID_FORMAT_PRICE_IS_NEGATIVE("[ERROR] 테이블 그룹핑을 위한 주문테이블은 최소 2개 이상이 되어야 합니다.");

    private final String errorMessage;

    CommonConstants(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
