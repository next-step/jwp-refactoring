package common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PriceTest {

    @Test
    void 가격_생성() {
        Price price = new Price(BigDecimal.valueOf(3000));

        assertAll(
                () -> assertThat(price).isNotNull(),
                () -> assertThat(price.getValue()).isEqualTo(BigDecimal.valueOf(3000))
        );
    }

    @Test
    void 가격이_NULL인_경우() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 가격이_음수인_경우() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력된 값보다 큰지 확인한다.")
    @CsvSource(value = {"2000,false", "3000,false", "4000,true"})
    @ParameterizedTest
    void isGatherThan(long priceValue, boolean expected) {
        Price price = new Price(BigDecimal.valueOf(priceValue));

        assertThat(price.isGatherThan(BigDecimal.valueOf(3000))).isEqualTo(expected);
    }

}
