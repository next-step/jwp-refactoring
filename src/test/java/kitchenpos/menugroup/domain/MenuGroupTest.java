package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuGroupTest {

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        String name = "추천메뉴";

        // when
        MenuGroup menuGroup = new MenuGroup(name);

        // then
        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 이름 없이는 생성이 불가능하다.")
    @Test
    void required() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> new MenuGroup(null));
        assertThrows(IllegalArgumentException.class, () -> new MenuGroup(""));
    }
}
