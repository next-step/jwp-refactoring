package kitchenpos.menu.application;

import static kitchenpos.ServiceTestFactory.FAVORITE_MENU_GROUP;
import static kitchenpos.ServiceTestFactory.NEW_MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.menu.dao.FakeMenuGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupServiceTest {
    private final MenuGroupService menuGroupService = new MenuGroupService(new FakeMenuGroupDao());

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        //when
        MenuGroup actual = menuGroupService.create(NEW_MENU_GROUP);
        //then
        assertThat(actual.getName()).isEqualTo(NEW_MENU_GROUP.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void findAll() {
        //given
        List<String> expectedNames = findMenuGroupNames(Arrays.asList(NEW_MENU_GROUP, FAVORITE_MENU_GROUP));

        //when
        List<MenuGroup> actual = menuGroupService.list();
        List<String> actualNames = findMenuGroupNames(actual);

        //then
        assertThat(actualNames).containsExactlyElementsOf(expectedNames);
    }

    private List<String> findMenuGroupNames(List<MenuGroup> actual) {
        return actual.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
    }
}
