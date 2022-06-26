package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("제품이 동일한지 검증")
    void verifyEqualsProduct() {
        final Product product = new Product(1L, "상품이름", Price.of(1_000L));

        assertThat(product).isEqualTo(new Product(1L, "상품이름", Price.of(1_000L)));
    }
}
