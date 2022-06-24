package kitchenpos.domain.menu;

import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuProduct.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("price가 null이거나 0보다 작을 경우 예외 발생")
    @Test
    void validateForCreate() {
        // given
        Menu menu1 = new Menu(1L, "test", null, new MenuGroup(), new MenuProducts());

        // when && then
        assertThatThrownBy(() -> menu1.validateForCreate())
                .isInstanceOf(IllegalArgumentException.class);

        // given
        Menu menu2 = new Menu(1L, "test", BigDecimal.valueOf(-1), new MenuGroup(), new MenuProducts());

        // when && then
        assertThatThrownBy(() -> menu2.validateForCreate())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
