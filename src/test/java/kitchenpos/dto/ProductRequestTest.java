package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRequestTest {
    @Test
    @DisplayName("상품 가격이 NULL 이거나 음수면 생성할 수 없다.")
    void create_fail() {
        assertThatThrownBy(() -> new ProductRequest("product", null)).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ProductRequest("product", BigDecimal.valueOf(-1))).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
