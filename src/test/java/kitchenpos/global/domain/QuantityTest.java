package kitchenpos.global.domain;

import kitchenpos.menu.message.QuantityMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {

    @Test
    @DisplayName("수량 생성에 성공한다.")
    void createQuantityTest() {
        // when
        Quantity quantity = Quantity.of(2L);

        // then
        assertThat(quantity).isEqualTo(Quantity.of(2L));
    }

    @Test
    @DisplayName("가격 생성시 0원 미만의 가격이 주어진 경우 예외처리되어 생성에 실패한다.")
    void createQuantityThrownByInvalidQuantityTest() {
        // when & then
        assertThatThrownBy(() -> Quantity.of(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuantityMessage.CREATE_ERROR_QTY_MUST_BE_GREATER_THAN_ZERO.message());
    }
}
