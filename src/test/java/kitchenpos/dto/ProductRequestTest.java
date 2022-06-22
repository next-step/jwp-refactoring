package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRequestTest {
    @Test
    @DisplayName("상품 가격이 비어있거나 음수면 생성할 수 없다.")
    void create_fail_price() {
        assertThatThrownBy(() -> new ProductRequest("product", null)).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ProductRequest("product", BigDecimal.valueOf(-1))).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("상품 이름이 비어있으면 생성할 수 없다.")
    void create_fail_name() {
        assertThatThrownBy(() -> new ProductRequest(null, BigDecimal.valueOf(1000))).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
