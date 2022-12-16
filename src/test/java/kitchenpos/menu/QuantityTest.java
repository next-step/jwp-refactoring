package kitchenpos.menu;

import kitchenpos.menu.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuantityTest {

    @DisplayName("음수 수량 생성 오류 테스트")
    @Test
    void createMinusQuantityExceptionTEst() {
        assertThatThrownBy(() -> new Quantity(-10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량 0 생성 오류 테스트")
    @Test
    void createZeroQuantityExceptionTEst() {
        assertThatThrownBy(() -> new Quantity(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
