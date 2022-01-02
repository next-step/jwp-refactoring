package kitchenpos.menu.application;

import common.domain.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.exception.GreaterProductSumPriceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MenuValidator {

    private final ProductService productService;

    public MenuValidator(final ProductService productService) {
        this.productService = productService;
    }

    public void isOverPrice(final Menu menu) {
        final Price totalPrice = getTotalPrice(menu.getMenuProducts());
        if (menu.getPrice().isGreaterThan(totalPrice)) {
            throw new GreaterProductSumPriceException();
        }
    }

    private Price getTotalPrice(final MenuProducts menuProducts) {
        return menuProducts.toList().stream()
                .map(this::calculatePrice)
                .reduce(Price.ZERO, Price::add);
    }

    private Price calculatePrice(final MenuProduct menuProduct) {
        final Product product = productService.getById(menuProduct.getProductId());
        return product.getPrice().multiply(menuProduct.getQuantity().toLong());
    }
}
