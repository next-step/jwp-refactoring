package kitchenpos.event.product;

import kitchenpos.domain.product.ProductService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Quantity;
import kitchenpos.exception.InvalidPriceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MenuEventHandler {

    private final ProductService productService;

    public MenuEventHandler(ProductService productService) {
        this.productService = productService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createMenuEvent(MenuCreatedEvent event) {
        Menu menu = event.getMenu();
        Price menuPrice = menu.getPrice();

        MenuProducts menuProducts = menu.getMenuProducts();
        kitchenpos.domain.product.Price price = menuProducts.getValue()
                .stream()
                .map(menuProduct -> {
                    Quantity quantity = menuProduct.getQuantity();
                    return productService.getProductPrice(menuProduct.getProductId(), quantity);
                }).reduce(kitchenpos.domain.product.Price::add).orElse(kitchenpos.domain.product.Price.of(0L));

        if (menuPrice.getValue().compareTo(price.getValue()) > 0) {
            throw new InvalidPriceException("Total Price is higher then expected MenuProduct Price");
        }
    }
}
