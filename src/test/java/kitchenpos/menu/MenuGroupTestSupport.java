package kitchenpos.menu;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.test.util.ReflectionTestUtils;

public class MenuGroupTestSupport {

    /**
     * 새로운 메뉴 그룹을 만듭니다.
     * @param name
     * @return 상품
     */
    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        ReflectionTestUtils.setField(menuGroup, "name", name);

        return menuGroup;
    }
}
