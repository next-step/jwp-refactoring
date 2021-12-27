package kitchenpos.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.PriceNotAcceptableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격 생성")
    @Test
    void constructor() {
        BigDecimal value = BigDecimal.valueOf(1000L);
        Price price = Price.valueOf(value);
        assertThat(price).isEqualTo(Price.valueOf(value));
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @Test
    void constructor_exception() {
        BigDecimal value = BigDecimal.valueOf(-1L);
        assertThatThrownBy(() -> Price.valueOf(value))
            .isInstanceOf(PriceNotAcceptableException.class);
    }

    @DisplayName("가격 합계 기능")
    @Test
    void sumPrices() {
        List<Price> prices = Arrays.asList(
            Price.valueOf(BigDecimal.valueOf(1000)),
            Price.valueOf(BigDecimal.valueOf(5100)),
            Price.valueOf(BigDecimal.valueOf(10000))
        );
        Price sum = Price.sumPrices(prices);
        assertThat(sum).isEqualTo(Price.valueOf(BigDecimal.valueOf(16100)));
    }
}
