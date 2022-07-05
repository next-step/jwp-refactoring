package kitchenpos.menus.menu.dto;

import kitchenpos.menus.menu.domain.Menu;

public class OrderMenuResponse {

    private final Long menuId;
    private final String menuName;
    private final Long menuPrice;

    public OrderMenuResponse(Long menuId, String menuName, Long menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderMenuResponse toOrderMenu(Menu menu) {
        return new OrderMenuResponse(menu.getId(), menu.getName(), menu.getPrice().longValue());
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Long getMenuPrice() {
        return menuPrice;
    }
}
