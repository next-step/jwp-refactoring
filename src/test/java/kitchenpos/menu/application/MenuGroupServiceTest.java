package kitchenpos.menu.application;

import static kitchenpos.ServiceTestFactory.createMenuGroupBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dao.FakeMenuGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupServiceTest {
    private final MenuGroupService menuGroupService = new MenuGroupService(new FakeMenuGroupDao());
    private MenuGroup newMenuGroup;
    private MenuGroup favoriteMenuGroup;

    @BeforeEach
    void setUp() {
        newMenuGroup = createMenuGroupBy(1L, "신메뉴");
        favoriteMenuGroup = createMenuGroupBy(2L, "인기메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        //when
        MenuGroup actual = menuGroupService.create(newMenuGroup);
        //then
        assertThat(actual.getName()).isEqualTo(newMenuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void findAll() {
        //given
        menuGroupService.create(newMenuGroup);
        menuGroupService.create(favoriteMenuGroup);
        List<String> expectedNames = findMenuGroupNames(Arrays.asList(newMenuGroup, favoriteMenuGroup));

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
