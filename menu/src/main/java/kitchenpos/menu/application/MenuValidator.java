package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private static final int COMPARE_EQUAL = 0;

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public void validateCreateMenu(Menu menu) {
        checkExistMenuGroup(menu.getMenuGroupId());
        checkOverPrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void checkExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다");
        }
    }

    private void checkOverPrice(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = getTotalPrice(menuProducts);

        if (menuPrice.compareTo(totalPrice) > COMPARE_EQUAL) {
            throw new IllegalArgumentException("메뉴 금액이 전체 메뉴 상품 금액의 합보다 많습니다.");
        }
    }

    private BigDecimal getTotalPrice(List<MenuProduct> menuProducts) {
        List<Product> products = getProducts(menuProducts);

        return menuProducts.stream()
                .map(getMenuProductPrice(products))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<Product> getProducts(List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());

        return productRepository.findAllById(productIds);
    }

    private Function<MenuProduct, BigDecimal> getMenuProductPrice(List<Product> products) {
        return menuProduct -> {
            Product product = findMatchProduct(products, menuProduct);
            BigDecimal productPrice = product.getPrice();

            return productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
        };
    }

    private Product findMatchProduct(List<Product> products, MenuProduct menuProduct) {
        return products.stream()
                .filter(product -> menuProduct.isEqualProductId(product.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
