package kitchenpos.menu.validator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(findMenuGroupById(menuRequest.getMenuGroupId()));
        validateMenuProducts(menuRequest);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(ErrorCode.MENU_GROUP_NOT_EMPTY.getErrorMessage());
        }
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new NotFoundException());
    }

    private void validateMenuProducts(MenuRequest menuRequest) {
        Price price = Price.from(menuRequest.getPrice());
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(menuRequest.getMenuProducts()));
        if(price.isBiggerThan(menuProducts.totalPrice())) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRICE_MORE_THAN_TOTAL_PRICE.getErrorMessage());
        }
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(menuProductRequest -> {
                Long productId = menuProductRequest.getProductId();
                return menuProductRequest.toMenuProduct(findProductById(productId));
            })
            .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }
}
