package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void checkMenuGroup(final MenuRequest menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException(MENU_GROUP_IS_NOT_EXIST);
        }
    }

    public void checkPrice(final MenuRequest menu) {
        if (Price.valueOf(menu.getPrice()).compareTo(totalMenuProductsPrice(menu.getMenuProducts())) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_IS_TOO_EXPENSIVE);
        }
    }

    private Price totalMenuProductsPrice(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(this::menuProductPrice)
                .reduce(Price::add)
                .orElse(Price.ZERO);
    }

    private Price menuProductPrice(final MenuProductRequest menuProduct) {
        return productPrice(menuProduct.getProductId())
                .multiply(Price.valueOf(menuProduct.getQuantity()));
    }

    private Price productPrice(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(PRODUCT_IS_NOT_EXIST))
                .getPrice();
    }
}
