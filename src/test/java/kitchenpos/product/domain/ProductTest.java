package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("제품이 동일한지 검증")
    void verifyEqualsProduct() {
        final ProductV2 product = new ProductV2(1L, "상품이름", 1_000L);

        assertThat(product).isEqualTo(new ProductV2(1L, "상품이름", 1_000L));
    }
}
