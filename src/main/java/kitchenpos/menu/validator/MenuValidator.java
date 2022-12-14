package kitchenpos.menu.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void createMenu(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
//        validateMenuProducts(new Price(menuRequest.getPrice()), menuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(ErrorCode.MENU_GROUP_IS_NOT_EXIST.getMessage());
        }
    }

    private void validateMenuProducts(Price price, MenuProducts menuProducts) {
        if (menuProducts.get().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRODUCT_IS_EMPTY.getMessage());
        }

        if (price.isBiggerThan(menuProducts.totalMenuPrice())) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE.getMessage());
        }
    }
}
