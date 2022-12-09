package kitchenpos.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 테스트")
class ProductTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        Product product1 = new Product(1L, "product1", BigDecimal.ONE);
        Product product2 = new Product(1L, "product1", BigDecimal.ONE);

        Assertions.assertThat(product1).isEqualTo(product2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        Product product1 = new Product(1L, "product1", BigDecimal.ONE);
        Product product2 = new Product(2L, "product1", BigDecimal.ONE);

        Assertions.assertThat(product1).isNotEqualTo(product2);
    }
}
