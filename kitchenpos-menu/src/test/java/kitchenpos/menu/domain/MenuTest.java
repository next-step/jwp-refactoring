package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    @DisplayName("메뉴를 생성하고 생성자의 값을 리턴한다.")
    @Test
    void create() {
        MenuProduct menuProduct = new MenuProduct(1L, 1L);
        Menu menu = Menu.of("추천메뉴", BigDecimal.valueOf(16000), null);

        assertThat(menu).isNotNull();
    }
}
