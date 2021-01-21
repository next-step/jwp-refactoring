package kitchenpos.product.domain;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    private Product 비정상가격제품;

    @BeforeEach
    void setUp() {
        비정상가격제품 = new Product("비정상", new BigDecimal(-400));
    }

    @Test
    void validationCheck() {
        assertThatThrownBy(() -> {
            비정상가격제품.validationCheck();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
