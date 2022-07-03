package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    @DisplayName("상품 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // when then
        assertAll(
            () -> assertThatThrownBy(() -> new Product("떡볶이", new BigDecimal(-3_000L)))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> new Product("떡볶이", null))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
