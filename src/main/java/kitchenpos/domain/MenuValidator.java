package kitchenpos.domain;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void checkMenuGroup(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void checkPrice(final Menu menu) {
        if (menu.moreExpensiveThen(totalMenuProductsPrice(menu))) {
            throw new IllegalArgumentException();
        }
    }

    private Price totalMenuProductsPrice(final Menu menu) {
        return menu.getMenuProducts().stream()
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
                .orElseThrow(IllegalArgumentException::new)
                .getPrice();
    }
}
