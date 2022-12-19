package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        menu.validateName();

        final MenuPrice menuPrice = menu.getPrice();

        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final MenuProducts menuProducts = menu.getMenuProducts();

        MenuPrice sum = new MenuPrice(BigDecimal.ZERO);
        menuProducts.stream()
                .forEach(menuProduct -> {
                    final Product product = productRepository.findById(menuProduct.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
                });

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
