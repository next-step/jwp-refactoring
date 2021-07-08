package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품을 만든다.")
    @Test
    void create() {
        Product product = new Product(1L, "1번상품", BigDecimal.valueOf(10000));

        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }

    @DisplayName("상품 가격이 올바르지 않으면 상품을 만들 수 없다.")
    @Test
    void 상품_만들기가_불가하다() {
        assertThatThrownBy(() -> {
            new Product(1L, "1번상품", null);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            new Product(1L, "1번상품", BigDecimal.valueOf(-1));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
