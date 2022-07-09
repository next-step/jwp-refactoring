package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    @DisplayName("가격 비교")
    void isBigThen() {
        assertAll(
                () -> assertThat(Price.from(4).isBigThen(Price.from(3))).isTrue(),
                () -> assertThat(Price.from(3).isBigThen(Price.from(3))).isFalse(),
                () -> assertThat(Price.from(3).isBigThen(Price.from(4))).isFalse(),
                () -> assertThat(Price.from(3).isBigThen(Amount.from(4))).isFalse(),
                () -> assertThat(Price.from(4).isBigThen(Amount.from(3))).isTrue()
        );
    }

}
