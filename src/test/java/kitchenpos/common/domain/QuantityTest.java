package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("수량 도메인 테스트")
class QuantityTest {

    @Test
    @DisplayName("수량을 생성한다.")
    void create() {
        // when
        Quantity quantity = new Quantity(5);

        // then
        assertThat(quantity).isEqualTo(new Quantity(5));
    }

    @Test
    @DisplayName("0보다 작은 숫로 수량을 생성하면 예외를 발생한다.")
    void createThrowException() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-1));
    }

}
