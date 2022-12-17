package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품_가격이_없으면_안됨() {
        assertThatThrownBy(() -> new Product("치킨", null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_음수면_안됨() {
        assertThatThrownBy(() -> new Product("치킨", new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}