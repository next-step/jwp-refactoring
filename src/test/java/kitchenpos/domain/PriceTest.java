package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {
    private static final Price PRICE = new Price(BigDecimal.valueOf(1000));
    private static final Price CHEAPER = new Price(BigDecimal.valueOf(500));

    @Test
    void 가격은_0원_이상이어야_한다() {
        // when & then
        assertThatThrownBy(() ->
                new Price(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 가격이_더_비싼지_확인한다() {
        // when
        boolean result = PRICE.isExpensive(CHEAPER);

        // then
        assertThat(result).isTrue();
    }
}
