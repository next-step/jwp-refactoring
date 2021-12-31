package kitchenpos.validator;

import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuValidator {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuValidator(
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validateMenuProducts(menu);
    }

    public void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupService.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
    }

    public void validateMenuProducts(Menu menu) {
        List<Product> products = getValidProducts(menu.getProductIds());
        menu.validateTotalPrice(products);
    }

    private List<Product> getValidProducts(List<Long> productIds) {
        List<Product> products = productService.findAllByIdIn(productIds);
        if (productIds.size() != products.size()) {
            throw new NotFoundProductException();
        }
        return products;
    }
}
