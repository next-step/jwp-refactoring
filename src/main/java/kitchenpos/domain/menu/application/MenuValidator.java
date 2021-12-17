package kitchenpos.domain.menu.application;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu_group.domain.MenuGroup;
import kitchenpos.domain.menu_group.domain.MenuGroupRepository;
import kitchenpos.domain.product.domain.Product;
import kitchenpos.domain.product.domain.ProductRepository;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validate(menu, getMenuGroup(menu));
    }

    private void validate(Menu menu, MenuGroup menuGroup) {
        menu.checkPrice(calculateSum(menu));
    }

    private BigDecimal calculateSum(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = getProduct(menuProduct.getProductId());
                    return menuProduct.calculatePrice(product.getPrice());
                }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private MenuGroup getMenuGroup(Menu menu) {
        return menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_GROUP_NOT_FOUND));
    }
}
