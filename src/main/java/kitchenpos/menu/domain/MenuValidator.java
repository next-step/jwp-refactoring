package kitchenpos.menu.domain;

import kitchenpos.exception.CannotFindException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.Message.*;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validate(menu, getProducts(menu));
    }

    private void validateMenuGroup(Menu menu) {
        menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(() -> new CannotFindException(ERROR_MENUGROUP_NOT_FOUND));
    }

    private void validate(Menu menu, List<Product> products) {
        if (menu.getMenuProductsSize() != products.size()) {
            throw new CannotFindException(ERROR_PRODUCT_NOT_FOUND);
        }

        Price productsTotalPrice = getProductsPrice(products);
        if (menu.comparePriceTo(productsTotalPrice) > 0) {
            throw new IllegalArgumentException(ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
        }
    }

    private Price getProductsPrice(List<Product> products) {
        Price sum = Price.valueOf(0);
        for (Product product : products) {
            sum = sum.add(product.getPrice());
        }
        return sum;
    }

    private List<Product> getProducts(Menu menu) {
        List<MenuProduct> menuProducts = getMenuProducts(menu);
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        return productRepository.findAllById(productIds);
    }

    private List<MenuProduct> getMenuProducts(Menu menu) {
        return menu.getMenuProducts().values();
    }
}
