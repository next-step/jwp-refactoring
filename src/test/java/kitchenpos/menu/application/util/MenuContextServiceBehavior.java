package kitchenpos.menu.application.util;

import java.util.List;
import kitchenpos.menu.fixture.MenuFixtureFactory;
import kitchenpos.menu.fixture.MenuGroupFixtureFactory;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuContextServiceBehavior {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    public MenuGroup 메뉴그룹_생성됨(String name) {
        return menuGroupService.create(MenuGroupFixtureFactory.createMenuGroup(name));
    }

    public MenuDto 메뉴_생성됨(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProduct> menuProducts) {
        Menu menuFixture = MenuFixtureFactory.createMenu(menuGroup, menuName, menuPrice, menuProducts);
        return menuService.create(MenuDto.of(menuFixture));
    }
}
