package kitchenpos.domain.menus.menuGroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹")
class MenuGroupTest {
    @Test
    @DisplayName("메뉴그룹를 생성한다.")
    public void initMenuGroup() {
        // when
        MenuGroup menuGroup = new MenuGroup(getRandomId(), "치킨 세트");

        // then
        assertThat(menuGroup).isNotNull();
    }
}