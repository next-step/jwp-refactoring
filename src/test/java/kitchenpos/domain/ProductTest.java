package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void 상품_생성() {
        Product product = new Product("치킨", BigDecimal.valueOf(5000L));

        assertThat(product).isNotNull();
    }

    @Test
    void 이름이_없는경우() {
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(5000L)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 가격이_음수인경우() {
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(-5000L)))
                .isInstanceOf(RuntimeException.class);
    }
}