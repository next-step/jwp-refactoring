package kitchenpos.product.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 단위 테스트")
class ProductTest {
    @DisplayName("상품의 가격은 0원 이상이어야 한다")
    @Test
    void createPriceZero() {
        assertThatThrownBy(() -> new Product("탕수육", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
