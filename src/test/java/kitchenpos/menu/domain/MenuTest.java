package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴가 같은지 검증")
    void verifyEqualsMenu() {
        final MenuGroup menuGroup = new MenuGroup();
        final Menu menu = new Menu.Builder("메뉴이름")
                .setId(1L)
                .setPrice(Price.of(10_000L))
                .setMenuGroup(menuGroup)
                .build();

        assertThat(menu).isEqualTo(new Menu.Builder("메뉴이름")
                .setId(1L)
                .setPrice(Price.of(10_000L))
                .setMenuGroup(menuGroup)
                .build());
    }
}
