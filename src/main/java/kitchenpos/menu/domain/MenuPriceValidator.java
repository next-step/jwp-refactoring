package kitchenpos.menu.domain;

import kitchenpos.product.application.ProductService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuPriceValidator {
    private final ProductService productService;

    public MenuPriceValidator(ProductService productService) {
        this.productService = productService;
    }

    public void validate(BigDecimal price, List<MenuProductRequest> menuProductRequests) {
        final BigDecimal totalPrice = calcTotalPrice(menuProductRequests);
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calcTotalPrice(List<MenuProductRequest> menuProductRequests) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productService.getProduct(menuProductRequest.getProductId());
            totalPrice = totalPrice.add(calcPrice(product.getPrice(), menuProductRequest.getQuantity()));
        }
        return totalPrice;
    }

    private BigDecimal calcPrice(BigDecimal price, long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
