package kitchenpos.menu.validator;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menuGroup.dao.MenuGroupRepository;
import kitchenpos.product.dao.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateMenu(Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validateProduct(menu);
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴의 메뉴 그룹이 존재하지 않습니다."));
    }

    private void validateProduct(Menu menu) {
        Price productTotalPrice = getTotalProductPrice(menu.getMenuProducts());
        Price menuPrice = menu.getPrice();

        validatePrice(productTotalPrice, menuPrice);
    }

    private Price getTotalProductPrice(MenuProducts menuProducts) {
        return menuProducts.getValue()
                .stream()
                .map(menuProduct -> productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("메뉴에 포함된 상품이 존재하지 않습니다."))
                        .getPrice()
                        .multiplyByQuantity(menuProduct.getQuantity())
                ).reduce(Price::add)
                .orElseThrow(() -> new IllegalArgumentException("메뉴에 상품이 포함되어 있지 않습니다."));
    }

    private void validatePrice(Price totalProductPrice, Price menuPrice) {
        if (totalProductPrice.isNotSame(menuPrice)) {
            throw new IllegalArgumentException("전체 메뉴 가격과 메뉴의 가격이 일치하지 않습니다.");
        }
    }
}
