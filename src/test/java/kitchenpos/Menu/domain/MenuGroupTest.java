package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능 테스트")
class MenuGroupTest {
    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        MenuGroup menu = 메뉴_그룹_생성(1L, "간장맛 시리즈");

        메뉴_그룹_생성됨(menu);
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    private void 메뉴_그룹_생성됨(MenuGroup resultMenu) {
        assertThat(resultMenu.getId()).isEqualTo(1L);
        assertThat(resultMenu.getName()).isEqualTo("간장맛 시리즈");
    }
}