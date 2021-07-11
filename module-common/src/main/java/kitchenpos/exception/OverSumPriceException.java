package kitchenpos.exception;

public class OverSumPriceException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String OVER_SUM_PRICE_EXCEPTION = "메뉴의 가격이 상품의 합보다 클 수 없습니다.";

    public OverSumPriceException() {
        super(OVER_SUM_PRICE_EXCEPTION);
    }

    public OverSumPriceException(String message) {
        super(message);
    }
}
