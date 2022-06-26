package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴가 같은지 검증")
    void verifyEqualsMenu() {
        final MenuGroup menuGroup = new MenuGroup();
        final Menu menu = new Menu(1L, "메뉴이름", 10_000L, menuGroup, null);

        assertThat(menu).isEqualTo(new Menu(1L, "메뉴이름", 10_000L, menuGroup, null));
    }
}
