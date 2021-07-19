package kitchenpos.menu.event;

import kitchenpos.menu.dto.MenuRequest;

public class CreateMenuEvent {

    private MenuRequest menuRequest;

    public CreateMenuEvent(MenuRequest menuRequest) {
        this.menuRequest = menuRequest;
    }

    public MenuRequest getMenuRequest() {
        return menuRequest;
    }
}
