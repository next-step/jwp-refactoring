package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;

public class MenuCreatedEvent {
    private Menu menu;

    public MenuCreatedEvent(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
