package kitchenpos.application;

import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest {
    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup menuGroup = MenuGroup.of("추천메뉴");
        MenuGroup result = menuGroupService.create(menuGroup);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("모든 메뉴 그룹 조회")
    @Test
    void list() {
        MenuGroup menuGroup1 = MenuGroup.of("추천메뉴1");
        MenuGroup menuGroup2 = MenuGroup.of("추천메뉴2");

        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        List<MenuGroup> list = menuGroupService.list();
        assertThat(list.size()).isEqualTo(2);
    }

}
