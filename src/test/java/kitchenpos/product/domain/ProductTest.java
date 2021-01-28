package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("상품 등록")
    void register() {
        Product product = new Product(1L,"강정치킨", new BigDecimal(-1));

        assertThatThrownBy(() -> {
            product.validationCheck();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
