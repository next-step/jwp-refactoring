package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격을 생성할 수 있다.")
    @Test
    void price() {
        //given
        BigDecimal price = BigDecimal.valueOf(1000);
        //when
        Price actual = Price.from(price);
        //then
        assertThat(actual.value()).isEqualTo(price);
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void priceValid(int price) {
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격을 더할 수 있다 ")
    @Test
    void sum() {
        //given
        Price 천원 = Price.from(BigDecimal.valueOf(1000));
        Price 이천원 = Price.from(BigDecimal.valueOf(2000));
        //when
        Price 삼천원 = 천원.sum(이천원);
        //then
        assertThat(삼천원).isEqualTo(Price.from(BigDecimal.valueOf(3000)));

    }

    @DisplayName("가격을 곱할 수 있다.")
    @Test
    void multiply() {
        //given
        Price 천원 = Price.from(BigDecimal.valueOf(1000));
        long 수량 = 3;
        //when
        Price 삼천원 = 천원.multiply(수량);
        //then
        assertThat(삼천원).isEqualTo(Price.from(BigDecimal.valueOf(3000)));
    }
}
