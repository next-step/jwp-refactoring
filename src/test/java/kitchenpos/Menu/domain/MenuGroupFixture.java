package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.test.util.ReflectionTestUtils;

public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        ReflectionTestUtils.setField(menuGroup, "id", id);
        return menuGroup;
    }


}
