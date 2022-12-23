package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;

import kitchenpos.menu.domain.OrderMenu;

public class OrderMenus {
    private final List<OrderMenu> orderMenus;

    private OrderMenus(List<OrderMenu> orderMenus) {
        this.orderMenus = orderMenus;
    }

    public static OrderMenus from(List<OrderMenu> orderMenus) {
        return new OrderMenus(orderMenus);
    }

    public Optional<OrderMenu> findByMenuId(Long menuId) {
        return orderMenus.stream()
            .filter(orderMenu -> orderMenu.hasMenuId(menuId))
            .findAny();
    }
}
