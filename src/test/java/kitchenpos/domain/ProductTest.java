package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제품 단위 테스트")
class ProductTest {

    @Test
    @DisplayName("가격이 없거나 음수이면 오류 발생")
    void negativePriceOrNull() {
        BigDecimal minus = BigDecimal.valueOf(-1);
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", minus));
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", null));
    }
}
