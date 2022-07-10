package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격은 0원보다 커야한다.")
    @ParameterizedTest()
    @ValueSource(ints = {0, -1})
    void priceValid(int value) {
        //given
        BigDecimal price = BigDecimal.valueOf(value);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Price.from(price));
    }

    @Test
    @DisplayName("가격은 필수이다")
    void priceIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Price.from(null));
    }

}
