package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "상품이름", Price.of(1_000L));
    }

    @Test
    @DisplayName("제품이 동일한지 검증")
    void verifyEqualsProduct() {
        assertThat(product).isEqualTo(new Product(1L, "상품이름", Price.of(1_000L)));
    }

    @Test
    @DisplayName("제품 수량만큼 금액이 잘 나오는지 검증")
    void verifyMultiplyPriceOfQuantity() {
        assertThat(product.multiply(10L)).isEqualTo(Price.of(10_000L));
    }
}
