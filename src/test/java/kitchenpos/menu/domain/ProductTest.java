package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    
    @DisplayName("상품 가격은 0원 이상이어야한다")
    @Test
    void 상품_가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Product.of("치킨", -6000);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }

}
