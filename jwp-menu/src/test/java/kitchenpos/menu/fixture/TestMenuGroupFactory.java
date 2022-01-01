package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestMenuGroupFactory {
    public static MenuGroup 메뉴그룹_생성(final String menuGroupName) {
        return MenuGroup.from(menuGroupName);
    }

    public static MenuGroup 메뉴그룹_조회됨(final Long id, final String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup 메뉴그룹_생성됨(final Long id, final String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroupRequest 메뉴그룹생성_요청(final String name) {
        return MenuGroupRequest.from(name);
    }

    public static MenuGroupResponse 메뉴그룹_응답(final MenuGroup menuGroup) {
        return MenuGroupResponse.from(menuGroup);
    }

    public static MenuGroupResponse 메뉴그룹_응답(final Long id, final String name) {
        return MenuGroupResponse.of(id, name);
    }

    public static List<MenuGroupResponse> 메뉴그룹목록_응답(final List<MenuGroup> menuGroups) {
        return MenuGroupResponse.from(menuGroups);
    }

    public static List<MenuGroupResponse> 메뉴그룹목록_응답(final int countMenuGroups) {
        final List<MenuGroupResponse> menuGroupResponses = new ArrayList<>();
        for (int i = 1; i <= countMenuGroups; i++) {
            menuGroupResponses.add(MenuGroupResponse.of(Long.valueOf(i), "메뉴그룹"));
        }
        return menuGroupResponses;
    }

    public static List<MenuGroup> 메뉴그룹목록_조회됨(final int menuGroupCount) {
        final List<MenuGroup> menuGroups = new ArrayList<>();
        for (int i = 1; i <= menuGroupCount; i++) {
            menuGroups.add(MenuGroup.of(Long.valueOf(i), "메뉴그룹"));
        }
        return menuGroups;
    }

    public static void 메뉴그룹_목록_확인됨(List<MenuGroupResponse> actual, List<MenuGroup> menuGroups) {
        assertThat(actual).hasSize(menuGroups.size());
    }

    public static void 메뉴그룹_생성_확인됨(MenuGroupResponse actual, MenuGroup menuGroup) {
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(actual.getName()).isEqualTo(menuGroup.getName())
        );
    }

    public static MenuGroupRequest 메뉴그룹_요청(String name) {
        return MenuGroupRequest.from(name);
    }
}
