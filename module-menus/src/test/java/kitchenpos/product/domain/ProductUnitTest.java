package kitchenpos.product.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.generic.price.domain.Price;

public class ProductUnitTest {
    @Test
    @DisplayName("가격이 없거나 음수이면 오류 발생")
    void negativePriceOrNull() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", Price.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", null));
    }

    @Test
    @DisplayName("제품 필수값이 없어서 생성 실패")
    void create_failed() {
        Assertions.assertThatThrownBy(() -> new Product(null, Price.valueOf(10)))
            .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new Product("ABC", null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
