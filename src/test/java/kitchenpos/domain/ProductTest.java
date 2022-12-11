package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void 생성() {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(16000));

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("후라이드");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16000));
    }
}
