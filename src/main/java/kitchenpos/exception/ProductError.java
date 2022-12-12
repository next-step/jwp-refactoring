package kitchenpos.exception;

public class ProductError {
    private static final String PREFIX = "[ERROR] ";

    public static final String INVALID_PRICE = PREFIX + "상품 가격은 0 이상으로 설정 가능합니다.";

    private ProductError() {

    }
}
