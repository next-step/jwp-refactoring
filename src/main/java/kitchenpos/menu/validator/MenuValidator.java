package kitchenpos.menu.validator;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;

import static kitchenpos.common.ErrorMessage.*;

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
            throw new IllegalArgumentException(INVALID_MENU_GROUP.getMessage());
        }
    }

    private void validateMenuProducts(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (menuRequest.getMenuProductsRequest().size() != menuProducts.value().size()) {
            throw new IllegalArgumentException(INVALID_MENU_PRODUCT.getMessage());
        }

        Price price = Price.from(menuRequest.getPrice());
        if (price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_LESS_THAN_SUM_OF_PRICE.getMessage());
        }
    }
}