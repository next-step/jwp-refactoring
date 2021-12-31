package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.event.MenuDecideEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MenuDecideEventHandler {

    @Async
    @EventListener
    @Transactional
    public void handle(MenuDecideEvent event) {
        List<MenuProduct> menuProducts = event.getMenuProducts();
        Menu menu = event.getMenu();
        menuProducts.forEach(menuProduct -> {
            menuProduct.decideMenu(menu);
        });
    }
}
