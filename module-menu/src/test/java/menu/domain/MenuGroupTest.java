package menu.domain;

import menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    @Test
    void 메뉴_그룹_생성() {
        MenuGroup menuGroup = new MenuGroup("세트");

        assertThat(menuGroup).isNotNull();
    }

    @Test
    void 메뉴_그룹_이름이_없는경우() {
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(RuntimeException.class);
    }
}