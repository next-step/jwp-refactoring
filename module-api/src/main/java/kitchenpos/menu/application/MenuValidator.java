package kitchenpos.menu.application;

import kitchenpos.menu.exeption.InvalidPrice;
import kitchenpos.menu.application.exception.MenuGroupNotFoundException;
import kitchenpos.menu.application.exception.ProductNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validatePrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateMenuGroup(Long menuGroupId) {
        boolean isExistMenuGroup = menuGroupRepository.existsById(menuGroupId);

        if (!isExistMenuGroup) {
            throw new MenuGroupNotFoundException();
        }
    }

    private void validatePrice(Price menuPrice, List<MenuProduct> menuProducts) {
        Price sum = calculateTotalPrice(menuProducts);

        if (!sum.isExpensiveThan(menuPrice.getMoney())) {
            throw new InvalidPrice("메뉴 가격은 상품 가격의 합보다 적어야 합니다.");
        }
    }

    private Price calculateTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    Product product = getProduct(menuProduct);
                    return getTotalPrice(product.getPrice(), menuProduct.getQuantity());
                })
                .reduce(Price::sum)
                .orElseGet(Price::zero);
    }

    private Price getTotalPrice(Price price, Long quantity) {
        return price.multiply(quantity);
    }

    private Product getProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
                .orElseThrow(ProductNotFoundException::new);
    }
}
