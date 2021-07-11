package kitchenpos.common.valueobject;

import kitchenpos.common.valueobject.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    void create() {
        //when
        Price price = Price.of(BigDecimal.valueOf(1000L));

        //then
        assertThat(price.getValue().longValue()).isEqualTo(1000L);
    }

    @DisplayName("가격은 필수값이며 0보다 작을 수 없습니다.")
    @Test
    void createException() {
        //given
        BigDecimal price = BigDecimal.valueOf(-1L);

        //when
        assertThatThrownBy(() -> Price.of(price))
                .isInstanceOf(NegativePriceException.class); //then
    }

    @DisplayName("수량에 따라 가격을 계산한다.")
    @Test
    void calculatePriceByQuantity() {
        //given
        Price price = Price.of(BigDecimal.valueOf(1000L));

        //when
        Price actual = price.calculatePriceByQuantity(Quantity.of(3L));

        //then
        assertThat(actual.getValue().longValue()).isEqualTo(3000L);
    }
}
