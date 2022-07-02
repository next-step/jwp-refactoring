package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuValidator {
    private static final String MENU_GROUP_IS_NOT_EXIST = "메뉴 그룹이 존재하지 않습니다";
    private static final String MENU_PRICE_IS_TOO_EXPENSIVE = "메뉴의 가격은 메뉴 상품의 금액의 합을 초과할 수 없습니다";
    private static final String PRODUCT_IS_NOT_EXIST = "메뉴 상품이 존재하지 않습니다";
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void checkMenuGroup(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException(MENU_GROUP_IS_NOT_EXIST);
        }
    }

    public void checkPrice(final Menu menu) {
        if (menu.moreExpensiveThen(totalMenuProductsPrice(menu))) {
            throw new IllegalArgumentException(MENU_PRICE_IS_TOO_EXPENSIVE);
        }
    }

    private Price totalMenuProductsPrice(final Menu menu) {
        return menu.getMenuProducts().getMenuProducts().stream()
                .map(this::menuProductPrice)
                .reduce(Price::add)
                .orElse(Price.ZERO);
    }

    private Price menuProductPrice(final MenuProduct menuProduct) {
        return productPrice(menuProduct.getProductId())
                .multiply(Price.valueOf(menuProduct.getQuantity()));
    }

    private Price productPrice(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(PRODUCT_IS_NOT_EXIST))
                .getPrice();
    }
}
