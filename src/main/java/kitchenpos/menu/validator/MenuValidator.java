package kitchenpos.menu.validator;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validateMenuProducts(menu);
    }

    private void validateMenuGroup(Long id) {
        menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("메뉴 그룹(%d)를 찾을 수 없습니다.", id)));
    }

    private void validateMenuProducts(Menu menu) {
        MenuProducts menuProducts = menu.getMenuProducts();

        Price sum = Price.create();
        for (MenuProduct menuProduct : menuProducts.value()) {
            Product product = findProductById(menuProduct.getProductId());
            sum = sum.add(product.calculateTotal(menuProduct.getQuantity()));
        }

        if (menu.getPrice().isGreaterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품의 전체 금액보다 클 수 없습니다.");
        }
    }

    private Product findProductById(Long menuProductId) {
        return productRepository.findById(menuProductId).orElseThrow(
                () -> new IllegalArgumentException(String.format("상품(%d)를 찾을 수 없습니다.", menuProductId)));
    }
}
