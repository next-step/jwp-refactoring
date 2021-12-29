package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    public void existMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOverPrice(Menu menu) {
        BigDecimal totalPrice = calculateTotalPrice(menu);
        BigDecimal menuPrice = menu.getPrice();

        if (menuPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateTotalPrice(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = productService.getProduct(menuProduct.getProductId());
                    return menuProduct.calculatePriceQuantity(product.getPrice());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
