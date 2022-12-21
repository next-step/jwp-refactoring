package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void 생성() {
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        assertThat(product.getName()).isEqualTo("후라이드");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16000));
    }
}
