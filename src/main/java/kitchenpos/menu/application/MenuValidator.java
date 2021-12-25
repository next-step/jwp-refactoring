package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu createMenu(MenuRequest menuRequest) {
        existMenuGroup(menuRequest.getMenuGroupId());
        final Menu menu = menuRequest.toMenu();
        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest.getMenuProducts());
        menu.addMenuProducts(menuProducts);
        validateOverPrice(menu);
        return menu;
    }

    public void existMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts(List<MenuProductRequest> requests) {
        List<MenuProduct> result = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : requests) {
            final Product product = getProduct(menuProductRequest.getProductId());
            result.add(new MenuProduct(product.getId(), menuProductRequest.getQuantity()));
        }
        return result;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private BigDecimal calculateTotalPrice(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = getProduct(menuProduct.getProductId());
                    return menuProduct.calculatePriceQuantity(product.getPrice());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOverPrice(Menu menu) {
        BigDecimal totalPrice = calculateTotalPrice(menu);
        BigDecimal menuPrice = menu.getPrice();

        if (menuPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
