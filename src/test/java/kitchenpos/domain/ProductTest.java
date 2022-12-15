package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.InvalidPriceException;

class ProductTest {

    @DisplayName("[상품 등록] 가격이 없거나 0 미만인 경우 등록할 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Product("test", null))
            .isInstanceOf(InvalidPriceException.class);
        assertThatThrownBy(() -> new Product("test", BigDecimal.valueOf(-100)))
            .isInstanceOf(InvalidPriceException.class);
        assertDoesNotThrow(() -> new Product("test", BigDecimal.valueOf(99)));

    }
}
