package kitchenpos.menu.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성")
    @Test
    void construct() {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Menu menu = new Menu("앙념반 후라이드반", BigDecimal.valueOf(15000), menuGroup);
        Menu expectMenu = new Menu("앙념반 후라이드반", BigDecimal.valueOf(15000), menuGroup);

        Assertions.assertThat(menu.getName()).isEqualTo(expectMenu.getName());
        Assertions.assertThat(menu.getMenuGroup()).isEqualTo(expectMenu.getMenuGroup());
    }
}
