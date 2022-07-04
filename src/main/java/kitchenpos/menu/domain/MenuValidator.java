package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        if (!existsMenuGroupById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        validateProductIds(menu);
    }


    private void validateProductIds(Menu menu) {
        List<Long> productIds = menu.getMenuProducts().getProductIds();
        Products products = new Products(findProductsByIdIn(productIds));

        if (!products.isSameSize(productIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> findProductsByIdIn(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    public boolean existsMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.existsById(menuGroupId);
    }

}
