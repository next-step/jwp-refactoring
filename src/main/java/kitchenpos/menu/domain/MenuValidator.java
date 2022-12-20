package kitchenpos.menu.domain;

import kitchenpos.global.domain.Price;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.message.MenuMessage;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository,
                         ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreateMenu(MenuCreateRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        validateMenuProducts(request.toPrice(), request.getMenuProducts());
    }

    private void validateMenuGroup(Long menuGroupId) {
        if(menuGroupId == null || !menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message());
        }
    }

    private void validateMenuProducts(Price menuPrice, List<MenuProductRequest> menuProducts) {
        Price productTotalPrice = getProductTotalPrice(menuProducts);
        if(menuPrice.isGreaterThan(productTotalPrice)) {
            throw new IllegalArgumentException(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message());
        }
    }

    private Price getProductTotalPrice(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProductRequest -> productRepository.findById(menuProductRequest.getProductId())
                        .orElseThrow(EntityNotFoundException::new)
                        .getPrice()
                        .multiplyQuantity(menuProductRequest.toQuantity()))
                .reduce(Price::add)
                .orElse(Price.zero());
    }
}
