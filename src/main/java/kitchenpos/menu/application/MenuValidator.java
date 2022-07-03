package kitchenpos.menu.application;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public static final String ERROR_PRICE_TOO_HIGH = "가격은 %d 초과일 수 없습니다.";

    public MenuValidator(ProductService productService, MenuGroupService menuGroupService) {
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    public void validate(Menu menu){
        validateMenuGroup(menu);
        validateProductPrice(menu);
    }

    private void validateMenuGroup(Menu menu) {
        menuGroupService.findMenuGroupById(menu.getMenuGroupId());
    }

    private void validateProductPrice(Menu menu) {
        int sumOfProductPrice = menu.getMenuProducts().stream().
                mapToInt(menuProduct -> {
                    Product product = productService.findProductById(menuProduct.getProductId());
                    return menuProduct.getQuantity() * product.getPrice();
                }).
                sum();

        if (menu.getPrice() > sumOfProductPrice) {
            throw new IllegalPriceException(String.format(ERROR_PRICE_TOO_HIGH, sumOfProductPrice));
        }
    }
}
