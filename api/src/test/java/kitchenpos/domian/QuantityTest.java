package kitchenpos.domian;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domian.Quantity;
import kitchenpos.error.NegativeValueException;

@DisplayName("수량 테스트")
class QuantityTest {

    @DisplayName("생성")
    @Test
    void create() {
        // given
        Quantity quantity = new Quantity(100L);
        // when
        // then
        assertThat(quantity).isEqualTo(new Quantity(100L));
    }

    @DisplayName("생성 실패 - 수량이 음수")
    @Test
    void createFailedByAmount() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Quantity(-100L))
                .isInstanceOf(NegativeValueException.class);
    }
}