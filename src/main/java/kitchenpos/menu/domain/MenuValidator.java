package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.InvalidValueException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductService productService;

    public MenuValidator(ProductService productService) {
        this.productService = productService;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuProductsPriceThanMenuPrice(menuRequest.getPrice(), menuRequest.toMenuProducts());
    }

    private void validateMenuProductsPriceThanMenuPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = calculateMenuProductsPrice(menuProducts);

        if(price.compareTo(totalPrice) > 0) {
            throw new InvalidValueException();
        }
    }

    private BigDecimal calculateMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(final MenuProduct menuProduct : menuProducts) {
            final Product product = productService.findProductById(menuProduct.getProductId());

            BigDecimal multiply = product.getPrice()
                .multiply(new BigDecimal(menuProduct.getQuantity()));

            totalPrice = totalPrice.add(multiply);
        }

        return totalPrice;
    }
}
