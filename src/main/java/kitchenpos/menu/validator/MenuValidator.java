package kitchenpos.menu.validator;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductService productService;

    public MenuValidator(ProductService productService) {
        this.productService = productService;
    }

    public void validateProduct(Price price, List<MenuProduct> menuProducts) {
        long priceSum = menuProducts.stream().
            mapToLong(menuProduct -> productService.findPriceByIdOrElseThrow(menuProduct.getProductId()) * menuProduct.getQuantity()).sum();
        if (price.isGreaterThan(priceSum)) {
            throw new IllegalArgumentException();
        }
    }
}
