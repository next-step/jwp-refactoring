package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void construct() {
        String menuGroupName = "추천메뉴";
        MenuGroup menuGroup = new MenuGroup(menuGroupName);
        MenuGroup expectedGroup = new MenuGroup(menuGroupName);
        assertThat(menuGroup.getName()).isEqualTo(expectedGroup.getName());
    }
}
