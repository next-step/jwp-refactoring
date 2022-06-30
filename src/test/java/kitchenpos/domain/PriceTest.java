package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    void 가격은_0원_이상이어야_한다() {
        // when & then
        assertThatThrownBy(() ->
                new Price(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0원 이상이어야 합니다.");
    }
}
