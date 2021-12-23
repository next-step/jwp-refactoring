package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
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

    public void registerValidate(Menu menu) {
        existMenuGroupValid(menu);
        emptyValid(menu);
        greaterThanMinPriceValid(menu);
    }

    private void existMenuGroupValid(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NotFoundException(CommonErrorCode.MENU_GROUP_NOT_FOUND_EXCEPTION);
        }
    }

    private void emptyValid(Menu menu) {
        if (Objects.isNull(menu.getName())) {
            throw new InvalidParameterException(CommonErrorCode.NOT_EMPTY);
        }

        if (Objects.isNull(menu.getMenuGroupId())) {
            throw new InvalidParameterException(CommonErrorCode.NOT_EMPTY);
        }
    }

    private void greaterThanMinPriceValid(Menu menu) {
        BigDecimal totalPrice = calculateTotalPrice(menu);
        Price menuPrice = menu.getPrice();
        if (menuPrice.greaterThanOf(totalPrice)) {
            throw new InvalidParameterException(CommonErrorCode.MENU_PRICE_OVER_RANGE_EXCEPTION);
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
            .orElseThrow(() -> new InvalidParameterException(CommonErrorCode.PRODUCT_NOT_FOUND));
    }
}
