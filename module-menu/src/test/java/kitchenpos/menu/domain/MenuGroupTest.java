package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴그룹이 같은지 검증")
    void verifyEqualsMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");

        assertThat(menuGroup).isEqualTo(new MenuGroup(1L, "메뉴그룹"));
    }
}
