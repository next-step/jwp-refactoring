package kitchenpos.validator.menu.impl;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.validator.menu.MenuValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ProductsPriceValidator extends MenuValidator {

    private final ProductRepository productRepository;

    public ProductsPriceValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void validate(Menu menu) {
        BigDecimal price = menu.getPrice();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal sumMenuProductsPrice = makeSumMenuProductsPrice(menuProducts);
        if (price.compareTo(sumMenuProductsPrice) > 0) {
            throw new IllegalArgumentException(
                    "메뉴의 가격은 메뉴상품들 가격의 합보다 낮아야 합니다[price:" + price + "/sumMenuProductsPrice:" + sumMenuProductsPrice
                            + "]");
        }
    }

    private BigDecimal makeSumMenuProductsPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "등록되지 않은 상품은 메뉴로 지정할 수 없습니다[productId:" + menuProduct.getProductId() + "]"))
                        .multiply(menuProduct.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
