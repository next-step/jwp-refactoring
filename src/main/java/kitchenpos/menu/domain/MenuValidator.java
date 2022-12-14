package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private static final String NOT_EXIST_PRODUCT = "상품이 존재하지 않습니다.";
    private static final String NOT_EXIST_MENU_GROUP = "메뉴 그룹이 존재하지 않습니다.";
    private static final String INVALID_MENU_PRICE = "메뉴의 가격이 메뉴 상품 가격의 총합보다 클 수 없습니다.";

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validatePrice(menu);
    }

    private void validateMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_GROUP);
        }
    }

    private void validatePrice(Menu menu) {
        if (menu.moreExpensive(totalMenuProductPrice(menu))) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE);
        }
    }

    private MenuPrice totalMenuProductPrice(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(this::menuProductPrice)
                .reduce(MenuPrice::add)
                .orElse(new MenuPrice(BigDecimal.ZERO));
    }

    private MenuPrice menuProductPrice(MenuProduct menuProduct) {
        return menuProduct.calculatePrice(productPrice(menuProduct.getProductId()));
    }

    private BigDecimal productPrice(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT))
                .getPrice();
    }
}
