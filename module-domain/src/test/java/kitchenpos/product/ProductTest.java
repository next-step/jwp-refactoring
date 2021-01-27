package kitchenpos.product;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @Test
    void validationCheck() {
        assertThatThrownBy(() -> {
            new Product("비정상", new BigDecimal(-400));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
