package kitchenpos.menugroup.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuGroupRequest {

    private String name;

    protected MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }

    public MenuGroup createMenuGroup() {
        return new MenuGroup(name);
    }
}
