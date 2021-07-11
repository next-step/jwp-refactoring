package kitchenpos.product.application;

import kitchenpos.common.model.Price;
import kitchenpos.menu.application.MenuCreatedEvent;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuEventHandler {
    private static final String INVALID_PRICE = "올바르지 않은 금액입니다.";
    private final ProductService productService;

    public MenuEventHandler(ProductService productService) {
        this.productService = productService;
    }

    @TransactionalEventListener
    public void createMenuEventListener(MenuCreatedEvent menuCreatedEvent) {
        Menu menu = menuCreatedEvent.getMenu();
        BigDecimal price = menu.getPrice();

        List<MenuProduct> menuProducts = menu.getMenuProducts();
        Price totalPrice = getTotalPrice(menuProducts);

        if (totalPrice.isBigger(price)) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    private Price getTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(v -> {
                    Long quantity = v.getQuantity();
                    return productService.getProductPrice(v.getProductId(), quantity);
                })
                .reduce(Price.ZERO, Price::add);
    }
}
