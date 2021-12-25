package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 도메인 테스트")
class MenuGroupTest {

    private static final String MENU_GROUP_NAME = "치킨류";

    @Test
    @DisplayName("메뉴 그룹 이름은 필수이다")
    void createValidateName() {
        assertThatThrownBy(() -> MenuGroup.from(null))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from(MENU_GROUP_NAME);
        assertTrue(menuGroup.equalName(MENU_GROUP_NAME));
    }

}