package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest request) {
        checkMenuGroupExist(request.getMenuGroupId());
        checkMenuPriceLessOrEqualTotalAmount(request);
    }

    private void checkMenuGroupExist(Long menuGroupId) {
        Optional<MenuGroup> menuGroup = menuGroupRepository.findById(menuGroupId);

        if (!menuGroup.isPresent()) {
            throw new EntityNotFoundException(ExceptionMessage.MENU_GROUP_NOT_FOUND);
        }
    }

    private void checkMenuPriceLessOrEqualTotalAmount(MenuRequest request) {
        BigDecimal amount = getAmount(request);
        MenuPrice price = MenuPrice.from(request.getPrice());
        price.checkLessOrEqualTotalAmount(amount);
    }

    private BigDecimal getAmount(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> findProductById(it.getProductId()).getPrice()
                        .multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND));
    }
}
