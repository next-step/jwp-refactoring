package kitchenpos.menu.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateCreateMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateMenuProducts(menuRequest, menuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(ErrorCode.MENU_GROUP_IS_NOT_EXIST.getMessage());
        }
    }

    private void validateMenuProducts(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (menuRequest.getMenuProducts().size() != menuProducts.get().size()) {
            throw new IllegalArgumentException(ErrorCode.PRODUCT_IS_NOT_EXIST.getMessage());
        }

        if (menuProducts.get().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRODUCT_IS_EMPTY.getMessage());
        }

        Price price = new Price(menuRequest.getPrice());
        if (price.isBiggerThan(menuProducts.totalMenuPrice())) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE.getMessage());
        }
    }
}
