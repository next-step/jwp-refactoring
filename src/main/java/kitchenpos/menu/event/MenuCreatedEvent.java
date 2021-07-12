package kitchenpos.menu.event;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menuproduct.dto.MenuProductRequest;

import java.util.List;

public class MenuCreatedEvent {
    private Menu menu;
    private List<MenuProductRequest> menuProductRequests;

    public MenuCreatedEvent(Menu menu, List<MenuProductRequest> menuProductRequests) {
        this.menu = menu;
        this.menuProductRequests = menuProductRequests;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
