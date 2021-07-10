package kitchenpos.event.product;

import kitchenpos.domain.menu.Menu;
import org.springframework.context.ApplicationEvent;

public class MenuCreatedEvent extends ApplicationEvent {

    private final Menu menu;

    public MenuCreatedEvent(Menu menu) {
        super(menu);
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
