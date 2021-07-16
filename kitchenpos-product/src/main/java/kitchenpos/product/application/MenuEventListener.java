package kitchenpos.product.application;

import kitchenpos.common.model.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGeneratedEvent;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuEventListener {
    private static final String INVALID_PRICE = "올바르지 않은 금액입니다.";
    private final ProductService productService;

    public MenuEventListener(ProductService productService) {
        this.productService = productService;
    }

    @EventListener
    public void generateMenuEventListener(MenuGeneratedEvent menuGeneratedEvent) {
        Menu menu = menuGeneratedEvent.getMenu();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        Price totalPrice = getTotalPrice(menuProducts);
        BigDecimal menuPrice = menu.getPrice();

        if (totalPrice.isBigger(menuPrice)) {
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
