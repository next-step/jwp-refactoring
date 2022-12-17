package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
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
        if(price.isBiggerThan(totalPrice(menuRequest.getMenuProducts()))) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRICE_MORE_THAN_TOTAL_PRICE.getErrorMessage());
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    private Price totalPrice(List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = Price.from(BigDecimal.ZERO);
        for(MenuProductRequest menuProductRequest: menuProductRequests) {
            Product product = findProductById(menuProductRequest.getProductId());
            Price productPrice = product.getPrice();
            totalPrice = totalPrice.add(productPrice.multiply(Quantity.from(menuProductRequest.getQuantity())));
        }
        return totalPrice;
    }
}
