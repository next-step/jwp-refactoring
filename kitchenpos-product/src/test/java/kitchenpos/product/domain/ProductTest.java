package kitchenpos.product.domain;

import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("상품 테스트")
class ProductTest {

    @DisplayName("id가 같은 두 객체는 같은 객체이다.")
    @Test
    void equalsTest() {
        Product product1 = Product.of(1L, "product1", BigDecimal.ONE);
        Product product2 = Product.of(1L, "product1", BigDecimal.ONE);

        Assertions.assertThat(product1).isEqualTo(product2);
    }

    @DisplayName("id가 다르면 두 객체는 다른 객체이다.")
    @Test
    void equalsTest2() {
        Product product1 = Product.of(1L, "product1", BigDecimal.ONE);
        Product product2 = Product.of(2L, "product1", BigDecimal.ONE);

        Assertions.assertThat(product1).isNotEqualTo(product2);
    }
}
