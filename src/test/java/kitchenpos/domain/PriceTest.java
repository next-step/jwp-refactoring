package kitchenpos.domain;

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
                .isThrownBy(()-> Price.of(price));
    }

    @Test
    @DisplayName("가격은 필수이다")
    void priceIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Price.of(null));
    }

    @Test
    @DisplayName("가격 비교")
    void isBigThen() {
        assertAll(
                () -> assertThat(Price.of(4).isBigThen(Price.of(3))).isTrue(),
                () -> assertThat(Price.of(3).isBigThen(Price.of(3))).isFalse(),
                () -> assertThat(Price.of(3).isBigThen(Price.of(4))).isFalse(),
                () -> assertThat(Price.of(3).isBigThen(Amount.of(4))).isFalse(),
                () -> assertThat(Price.of(4).isBigThen(Amount.of(3))).isTrue()
        );
    }

}