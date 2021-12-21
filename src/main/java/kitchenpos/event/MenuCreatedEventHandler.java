package kitchenpos.event;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuCreatedEventHandler {

    private final ProductService productService;

    public MenuCreatedEventHandler(ProductService productService) {
        this.productService = productService;
    }

    @EventListener
    public void createMenu(MenuCreatedEvent event) {

        Menu menu = event.getMenu();

        List<MenuProduct> menuProductList = menu.getMenuProducts().getMenuProducts()
                                                        .stream()
                                                        .map(menuProduct -> new MenuProduct(menuProduct.getMenu(), productService.getProduct(menuProduct.getProduct().getId()), menuProduct.getQuantity()))
                                                        .collect(Collectors.toList());
        menu.clearMenuProducts();
        menuProductList.forEach(menu::addMenuProduct);
        menu.getMenuProducts().checkOverPrice(menu.getPrice());
    }

}
