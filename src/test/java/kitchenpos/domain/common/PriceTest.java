package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("가격은 0원 이상이어야 한다.")
    void pricePositiveTest() {

        assertThatThrownBy(
                () -> new Price(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("덧셈 테스트")
    void multiply() {

        Price price1 = new Price(BigDecimal.valueOf(100));
        Price price2 = new Price(BigDecimal.valueOf(100));

        Price result = new Price(BigDecimal.valueOf(200));
        assertThat(price1.add(price2)).isEqualTo(result);
    }

    @Test
    @DisplayName("크기 비교 테스트")
    void greaterThan() {

        Price price1 = new Price(BigDecimal.valueOf(200));
        Price price2 = new Price(BigDecimal.valueOf(100));

        assertThat(price1.greaterThan(price2)).isTrue();
    }
}
