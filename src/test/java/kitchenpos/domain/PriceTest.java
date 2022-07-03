package kitchenpos.domain;

import kitchenpos.menu.domain.Price;
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

    @Test
    void 가격을_더한다() {
        // when
        Price result = PRICE.add(CHEAPER);

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(1500));
    }

    @Test
    void 가격에_수량을_곱한다() {
        // when
        Price result = PRICE.multiply(5);

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(5000));
    }
}
