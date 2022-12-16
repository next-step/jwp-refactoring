package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private static final String ERROR_MESSAGE_NOT_FOUND_MENU_GROUP_FORMAT = "메뉴 그룹을 찾을 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_PRODUCT_FORMAT = "상품을 찾을 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_MENU_IS_GREATER_THAN_TOTAL_PRICE = "상품 총 금액이 메뉴의 가격 보다 클 수 없습니다.";

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        validateExistMenuGroup(menu);
        validateTotalPrice(menu);
    }

    private void validateExistMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.menuGroupId())) {
            throw new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MENU_GROUP_FORMAT, menu.menuGroupId()));
        }
    }

    private void validateTotalPrice(Menu menu) {
        Price totalPrice = calculateTotalPrice(menu);
        Price menuPrice = menu.price();
        if (menuPrice.isGreaterThan(totalPrice)) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_IS_GREATER_THAN_TOTAL_PRICE);
        }
    }

    private Price calculateTotalPrice(Menu menu) {
        return menu.menuProducts().stream()
                .map(menuProduct -> {
                    Product product = findProduct(menuProduct.productId());
                    return menuProduct.price(product.price());
                })
                .reduce(Price.from(BigDecimal.ZERO), Price::sum);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_PRODUCT_FORMAT, productId)));
    }
}
