package kitchenpos.menugroup.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        String name = "그룹1";
        MenuGroup menuGroup = new MenuGroup(name);

        Assertions.assertThat(menuGroup.getName()).isEqualTo(name);
    }
}
