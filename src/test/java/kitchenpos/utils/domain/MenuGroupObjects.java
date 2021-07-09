package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

public class MenuGroupObjects {
    private final MenuGroup menuGroup1;
    private final MenuGroup menuGroup2;
    private final MenuGroup menuGroup3;
    private final MenuGroup menuGroup4;

    public MenuGroupObjects() {
        menuGroup1 = new MenuGroup("두마리메뉴");
        menuGroup2 = new MenuGroup("한마리메뉴");
        menuGroup3 = new MenuGroup("순살파닭두마리메뉴");
        menuGroup4 = new MenuGroup("신메뉴");
    }

    public MenuGroup getMenuGroup1() {
        return menuGroup1;
    }

    public MenuGroup getMenuGroup2() {
        return menuGroup2;
    }

    public MenuGroup getMenuGroup3() {
        return menuGroup3;
    }

    public MenuGroup getMenuGroup4() {
        return menuGroup4;
    }

    public MenuGroupRequest getMenuGroupRequest1() {
        return new MenuGroupRequest(menuGroup1.getName());
    }

    public MenuGroupRequest getMenuGroupRequest2() {
        return new MenuGroupRequest(menuGroup2.getName());
    }

    public MenuGroupRequest getMenuGroupRequest3() {
        return new MenuGroupRequest(menuGroup3.getName());
    }

    public MenuGroupRequest getMenuGroupRequest4() {
        return new MenuGroupRequest(menuGroup4.getName());
    }

    public List<MenuGroup> getMenuGroups() {
        return new ArrayList<>(Arrays.asList(menuGroup1, menuGroup2, menuGroup3, menuGroup4));
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return new ArrayList<>(Arrays.asList(menuGroup1, menuGroup2, menuGroup3, menuGroup4))
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
