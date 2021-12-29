package kitchenpos.menu.domain;

import kitchenpos.common.exception.Message;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Amount;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidation {

    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuValidation(ProductService productService,
        MenuGroupService menuGroupService) {
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    public void valid(Menu menu) {
        validExistsMenuGroup(menu.getMenuGroupId());
        validSumProductPrice(menu);
    }

    private void validExistsMenuGroup(Long menuGroupId) {
        menuGroupService.findByIdThrow(menuGroupId);
    }

    private void validSumProductPrice(Menu menu) {
        final Amount price = menu.getPrice();

        if (price.grateThan(sum(menu.getMenuProducts()))) {
            throw new IllegalArgumentException(Message.MENU_AMOUNT_IS_TOO_LAGE.getMessage());
        }
    }

    private Amount sum(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
            .map(s -> {
                Product savedProduct = productService.findByIdThrow(s.getProductId());
                return savedProduct.multiply(s.getQuantity());
            }).reduce(Amount.ZERO, Amount::add);
    }

}
