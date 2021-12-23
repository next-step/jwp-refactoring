package kitchenpos.domain;

import kitchenpos.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("가격이 음수인 상품을 생성한다")
    @Test
    void negativePriceTest() {
        assertThatThrownBy(() -> new Product("양념치킨", Price.from(-10_000))).isInstanceOf(NegativePriceException.class);
    }
}