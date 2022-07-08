package kitchenpos.menu.application.behavior;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.fixture.MenuDtoFixtureFactory;
import kitchenpos.menu.application.fixture.MenuGroupDtoFixtureFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuContextServiceBehavior {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    public MenuGroup 메뉴그룹_생성됨(String name) {
        return menuGroupService.create(MenuGroupDtoFixtureFactory.createMenuGroup(name));
    }

    public MenuDto 메뉴_생성됨(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProductDto> menuProductDtos) {
        MenuDto menuDto = MenuDtoFixtureFactory.createMenu(menuGroup, menuName, menuPrice, menuProductDtos);
        return menuService.create(menuDto);
    }
}
