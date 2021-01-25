package kitchenpos.product.domain;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    private Product 비정상가격제품;

    @Test
    void validationCheck() {
        assertThatThrownBy(() -> {
            new Product("비정상", new BigDecimal(-400));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
