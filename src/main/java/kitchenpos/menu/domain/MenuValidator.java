package kitchenpos.menu.domain;

import kitchenpos.common.Price;
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
        List<Long> productIds = menu.getMenuProducts().getProductIds();
        Products products = new Products(findProductsByIdIn(productIds));

        validate(menu, productIds, products);
    }

    private void validate(Menu menu, List<Long> productIds, Products products) {
        validateExistsMenuGroup(menu);
        validateProductSize(productIds, products);
        validateMenuProducts(menu, products);
    }

    private void validateExistsMenuGroup(Menu menu) {
        if (!existsMenuGroupById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }


    private void validateProductSize(List<Long> productIds, Products products) {
        if (!products.isSameSize(productIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> findProductsByIdIn(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    private boolean existsMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.existsById(menuGroupId);
    }

    private void validateMenuProducts(Menu menu, Products products) {
        if (menu.getPrice().moreExpensiveThan(totalPrice(menu.getMenuProducts(), products))) {
            throw new IllegalArgumentException();
        }
    }

    private Price totalPrice(MenuProducts menuProducts, Products products) {
        Price price = new Price();
        for (Product product : products.getProducts()) {
            price = price.plus(product.getPrice()
                    .multiply(menuProducts.getQuantity(product.getId())));
        }
        return price;
    }
}
