package kitchenpos.menu.domain.validator;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductGroup;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuPriceSumValidator implements MenuPriceValidator {
    private static final String ILLEGAL_PRICE_ERROR_MESSAGE = "가격은 포함된 구성된 상품들의 금액 보다 작거나 같아야 한다.";
    private final ProductService productService;

    public MenuPriceSumValidator(ProductService productService) {
        this.productService = productService;
    }

    public void validate(MenuPrice price, MenuProductGroup menuProducts) {
        final BigDecimal totalPrice = calcTotalPrice(menuProducts);
        if (price.isLessThen(totalPrice)) {
            throw new IllegalMenuPriceException(ILLEGAL_PRICE_ERROR_MESSAGE);
        }
    }

    private BigDecimal calcTotalPrice(MenuProductGroup menuProducts) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            final Product product = productService.getProduct(menuProduct.getProductId());
            totalPrice = totalPrice.add(calcPrice(product.getPrice(), menuProduct.getQuantity()));
        }
        return totalPrice;
    }

    private BigDecimal calcPrice(BigDecimal price, long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
