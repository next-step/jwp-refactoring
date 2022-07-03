package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuValidator(final MenuGroupService menuGroupService, final ProductService productService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public void validate(MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroup(menuRequest.getMenuGroupId());
        validateMenuGroup(menuGroup);
        validateMenuPrice(Price.from(menuRequest.getPrice()), menuRequest.getMenuProducts());
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("메뉴그룹이 있어야 합니다.");
        }
    }

    private void validateMenuPrice(Price price, List<MenuProductRequest> menuProducts) {
        if (price.compareTo(totalPrice(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.");
        }
    }

    private Price totalPrice(List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = Price.from(BigDecimal.ZERO);
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productService.findProduct(menuProductRequest.getProductId());
            totalPrice.add(product.price().multiply(Quantity.from(menuProductRequest.getQuantity())));
        }
        return totalPrice;
    }
}
