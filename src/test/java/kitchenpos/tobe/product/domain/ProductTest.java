package kitchenpos.tobe.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void create() {
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        assertThat(product).isNotNull();
    }
}
