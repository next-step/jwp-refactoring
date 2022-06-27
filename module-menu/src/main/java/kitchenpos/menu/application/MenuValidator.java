package kitchenpos.menu.application;

import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.exception.CreateMenuException;
import kitchenpos.menu.exception.CreateMenuProductException;
import kitchenpos.menu.exception.MenuPriceException;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private static final String MENU_GROUP_IS_NOT_NULL = "메뉴생성 시 메뉴그룹이 필수입니다.";
    private static final String PRODUCT_IS_NOT_NULL = "메뉴상품 생성 시 상품은 필수입니다.";

    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuValidator(final MenuGroupService menuGroupService,
                         final ProductService productService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public void validate(Menu menu) {
        MenuGroup menuGroup = menuGroupService.findMenuGroup(menu.getMenuGroupId());
        validateCreateMenu(menuGroup);
        validateMenuProducts(menu.findMenuProducts());
        validateMenuPrice(menu);
    }

    private void validateMenuProducts(List<MenuProduct> products) {
        List<Long> productIds = products.stream().map(MenuProduct::getProductId).collect(Collectors.toList());
        List<Product> findProducts = productService.findProducts(productIds);
        if (productIds.size() != findProducts.size()) {
            throw new CreateMenuProductException(PRODUCT_IS_NOT_NULL);
        }
    }

    private void validateCreateMenu(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new CreateMenuException(MENU_GROUP_IS_NOT_NULL);
        }
    }

    private void validateMenuPrice(Menu menu) {
        List<Long> productIds = menu.findMenuProducts().stream().map(MenuProduct::getProductId).collect(Collectors.toList());
        List<Product> findProducts = productService.findProducts(productIds);
        Map<Long, ProductPrice> productIdPriceMap = findProducts.stream().collect(toMap(Product::getId, Product::getPrice));

        BigDecimal sum = menu.findMenuProducts()
                .stream()
                .map(menuProduct -> menuProduct.calculateTotalPrice(productIdPriceMap.get(menuProduct.getProductId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.findPrice().compareTo(sum) > 0) {
            throw new MenuPriceException(menu.findPrice(), sum);
        }
    }
}
