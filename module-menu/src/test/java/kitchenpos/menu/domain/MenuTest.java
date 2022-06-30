package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("price가 null이거나 0보다 작을 경우 예외 발생")
    @Test
    void validateForCreate() {
        // when && then
        assertThatThrownBy(() -> new Menu(1L, "test", null, new MenuGroup(), new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);

        // when && then
        assertThatThrownBy(() -> new Menu(1L, "test", BigDecimal.valueOf(-1), new MenuGroup(), new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
