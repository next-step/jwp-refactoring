package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Product product = Product.from(1L, "양념", BigDecimal.TEN);
        assertThat(product).isEqualTo(product);
    }
}
