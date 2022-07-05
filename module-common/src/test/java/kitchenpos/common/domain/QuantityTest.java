package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @DisplayName("수량을 생성한다.")
    @Test
    void create() {
        //given
        Long element = 3L;

        //when
        Quantity quantity = new Quantity(element);

        //then
        assertAll(
                () -> assertThat(quantity).isNotNull(),
                () -> assertThat(quantity.get()).isEqualTo(element)
        );
    }

    @DisplayName("수량은 NULL 일 수 없다.")
    @Test
    void create_invalidNull() {
        //when & then
        assertThatThrownBy(() -> new Quantity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량 0 이상이어야 합니다.");
    }

    @DisplayName("수량은 0보다 작을 수 없다.")
    @Test
    void create_invalidMinus() {
        //given
        Long element = -1L;

        //when & then
        assertThatThrownBy(() -> new Quantity(element))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량 0 이상이어야 합니다.");
    }

}
