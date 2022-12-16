package kitchenpos.common.domain;

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
}
