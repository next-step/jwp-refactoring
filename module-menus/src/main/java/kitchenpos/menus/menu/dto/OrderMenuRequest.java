package kitchenpos.menus.menu.dto;

import java.util.List;

public class OrderMenuRequest {
    private final List<Long> menuIds;

    public OrderMenuRequest(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public static OrderMenuRequest of(List<Long> menuIds) {
        return new OrderMenuRequest(menuIds);
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}
