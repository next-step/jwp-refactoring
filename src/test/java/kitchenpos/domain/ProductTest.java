package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("상품 생성에 성공한다")
    void createProductTest() {
        // when
        Product product = Product.of("후라이드", 12_000L);

        // then
        assertThat(product).isEqualTo(Product.of("후라이드", 12_000L));
    }
}
