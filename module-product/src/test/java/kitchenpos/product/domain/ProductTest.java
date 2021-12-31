package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Product;

public class ProductTest {
    
    @DisplayName("상품 가격은 0원 이상이어야한다")
    @Test
    void 상품_가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Product.of("치킨", -6000L);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }

}
