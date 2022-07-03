package kitchenpos.domain.validator;

import java.math.BigDecimal;
import java.util.Objects;

public class PriceValidator {
    private static final int MINIMUM_PRICE = 0;
    public static final String INVALID_PRODUCT_PRICE_ERROR_MESSAGE = "상품 가격이 올바르지 않습니다. 0원 이상의 가격을 입력해주세요.";

    public static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_PRICE) {
            throw new IllegalArgumentException(INVALID_PRODUCT_PRICE_ERROR_MESSAGE);
        }
    }
}
