package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴의 가격이 음수면 예외 발생한다.")
    @Test
    void negativePrice() {
        assertThatThrownBy(() -> new Menu("메뉴", BigDecimal.valueOf(-1), new MenuGroup()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
