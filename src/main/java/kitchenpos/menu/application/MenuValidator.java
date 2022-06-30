package kitchenpos.menu.application;

import java.util.Set;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.infrastructure.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infrastructure.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository,
                         ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validatePrice(menu);
    }

    private void validateMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(Menu menu) {
        Price sum = Price.from(calculateTotalPrice(menu));
        Price price = menu.getPrice();
        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    private long calculateTotalPrice(Menu menu) {
        Set<MenuProduct> menuProducts = menu.getMenuProducts();

        return menuProducts.stream()
                .map(this::calculateProductPrice)
                .mapToLong(Price::getPriceLongValue)
                .sum();
    }

    private Price calculateProductPrice(MenuProduct menuProduct) {
        Product product = validateProduct(menuProduct);
        Price price = product.getPrice();
        price.multiply(menuProduct.getQuantity());
        return price;
    }

    private Product validateProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }
}
