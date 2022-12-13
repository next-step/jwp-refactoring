package kitchenpos.menu.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

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
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(ErrorCode.MENU_GROUP_IS_NOT_EXIST.getMessage());
        }
    }
}
