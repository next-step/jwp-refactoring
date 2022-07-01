package kitchenpos.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    void 가격이_0보다작은_상품을_등록할_수_없다() {
        Product 음수가격의_상품 = new Product();

        assertThatThrownBy(() -> 음수가격의_상품.setPrice(BigDecimal.valueOf(-1000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
