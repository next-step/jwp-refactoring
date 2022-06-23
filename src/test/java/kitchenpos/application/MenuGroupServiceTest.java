package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹생성() {
        String name = "메뉴그룹1";
        MenuGroup menuGroup = MenuGroupFixtureFactory.createMenuGroup(name);
        MenuGroup newMenuGroup = menuGroupService.create(menuGroup);
        assertThat(newMenuGroup.getName()).isEqualTo(name);
    }

    @Test
    void 메뉴그룹조회() {
        menuGroupService.create(MenuGroupFixtureFactory.createMenuGroup("메뉴그룹1"));
        menuGroupService.create(MenuGroupFixtureFactory.createMenuGroup("메뉴그룹2"));
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(2);
    }
}
