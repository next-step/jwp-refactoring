package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("가격 도메인 테스트")
class PriceTest {

    @Test
    @DisplayName("가격을 생성한다.")
    void create() {
        // when
        Price price = new Price(new BigDecimal(16_000));

        // then
        assertThat(price).isEqualTo(new Price(new BigDecimal(16_000)));
    }

    @Test
    @DisplayName("0보다 작은 숫자로 가격을 생성하면 예외를 발생한다.")
    void createThrowException() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(new BigDecimal(-1)));
    }
}
