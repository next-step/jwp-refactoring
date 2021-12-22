package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.product.application.ProductService;
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
            throw new IllegalMenuPriceException("가격은 포함된 구성된 상품들의 금액 보다 작거나 같아야 한다.");
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
