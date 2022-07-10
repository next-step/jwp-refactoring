package kitchenpos.menu.mapper;

import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menuGroup.dao.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuMapper(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu mapFrom(final MenuCreateRequest request) {
        Menu menu = request.of();

        validateMenuGroup(menu.getMenuGroupId());
        validateProduct(menu);

        return menu;
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴의 메뉴 그룹이 존재하지 않습니다.");
        }
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
