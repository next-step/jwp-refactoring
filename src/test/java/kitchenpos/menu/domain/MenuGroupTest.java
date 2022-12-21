package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupTest {
    @Test
    void 생성() {
        MenuGroup menuGroup = new MenuGroup(1L, "두마리메뉴");

        assertThat(menuGroup.getId()).isEqualTo(1L);
        assertThat(menuGroup.getName()).isEqualTo("두마리메뉴");
    }
}
