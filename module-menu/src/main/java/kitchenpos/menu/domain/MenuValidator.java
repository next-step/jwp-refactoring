package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.common.MenuErrorCode;
import kitchenpos.exception.CommonErrorCode;
import kitchenpos.exception.InvalidParameterException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

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

    public void validateRegister(Menu menu) {
        validateExistMenuGroup(menu);
        validateEmpty(menu);
        validateRegisterGreaterThanMinPrice(menu);
    }

    private void validateExistMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NotFoundException(MenuErrorCode.MENU_GROUP_NOT_FOUND_EXCEPTION);
        }
    }

    private void validateEmpty(Menu menu) {
        if (Objects.isNull(menu.getName())) {
            throw new InvalidParameterException(CommonErrorCode.NOT_EMPTY);
        }
    }

    private void validateRegisterGreaterThanMinPrice(Menu menu) {
        BigDecimal totalPrice = calculateTotalPrice(menu);
        Price menuPrice = menu.getPrice();
        if (menuPrice.greaterThanOf(totalPrice)) {
            throw new InvalidParameterException(MenuErrorCode.MENU_PRICE_OVER_RANGE_EXCEPTION);
        }
    }

    private BigDecimal calculateTotalPrice(Menu menu) {
        return menu.getMenuProducts().stream()
            .map(menuProduct -> {
                Product product = getProduct(menuProduct.getProductId());
                return menuProduct.getPrice(product.getPrice());
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new InvalidParameterException(MenuErrorCode.PRODUCT_NOT_FOUND));
    }
}
