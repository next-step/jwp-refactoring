package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("가격 클래스 테스트")
class PriceTest {

    @DisplayName("곱하기 테스트")
    @ParameterizedTest
    @CsvSource(value = {"3:300", "5:500", "10:1000"}, delimiter = ':')
    void multiply(Long multiply, Integer expected) {
        Price price = Price.fromInteger(100);
        assertThat(price.multiply(multiply)).isEqualTo(Price.fromInteger(expected));
    }

    @DisplayName("보다 작음")
    @ParameterizedTest
    @CsvSource(value = {"100:false", "200:false", "300:true"}, delimiter = ':')
    void isSmallerThan(Integer target, boolean expected) {
        Price price = Price.fromInteger(200);
        assertThat(price.isSmallerThan(Price.fromInteger(target))).isEqualTo(expected);
    }

    @DisplayName("보다 큼")
    @ParameterizedTest
    @CsvSource(value = {"100:true", "200:false", "300:false"}, delimiter = ':')
    void isGreaterThan(Integer target, boolean expected) {
        Price price = Price.fromInteger(200);
        assertThat(price.isGreaterThan(Price.fromInteger(target))).isEqualTo(expected);
    }

    @DisplayName("동등성 비교")
    @Test
    void testEquals() {
        Price price = Price.fromInteger(200);

        assertAll(
            () -> assertFalse(price.equals(200), "integer"),
            () -> assertFalse(price.equals(BigDecimal.valueOf(200)), "bigdecimal"),
            () -> assertTrue(price.equals(Price.fromInteger(200)), "price from integer"),
            () -> assertTrue(price.equals(Price.valueOf(BigDecimal.valueOf(200))), "price from bigdecimal")
        );
    }

    @DisplayName("정합성 체크")
    @Test
    void validate() {
        assertThatThrownBy(() -> Price.fromInteger(null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("가격은 필수입니다.");

        assertThatThrownBy(() -> Price.valueOf(null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("가격은 필수입니다.");

        assertThatThrownBy(() -> Price.fromInteger(-2))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessageContaining("이상이어야 합니다.");
    }
}