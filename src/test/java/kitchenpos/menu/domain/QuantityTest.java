package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("수량 테스트")
class QuantityTest {

    @Test
    @DisplayName("수량 동등성 비교")
    void equals() {
        Quantity quantity = Quantity.valueOf(100L);

        assertAll(
            () -> assertTrue(quantity.equals(100L)),
            () -> assertTrue(quantity.equals(Quantity.valueOf(100L))),
            () -> assertFalse(quantity.equals(101))
        );
    }

    @Test
    @DisplayName("수량 정합성 체크")
    void validate() {
        assertThatThrownBy(() -> Quantity.valueOf(-1L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessageContaining("보다 많아야 합니다.");

        assertThatThrownBy(() -> Quantity.valueOf(null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("수량은 필수입니다.");
    }
}