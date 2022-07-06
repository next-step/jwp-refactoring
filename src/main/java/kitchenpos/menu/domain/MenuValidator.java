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
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다");
        }
    }


    private void validateProductSize(List<Long> productIds, Products products) {
        if (!products.isSameSize(productIds)) {
            throw new IllegalArgumentException("등록된 상품과 일치하지 않습니다");
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
            throw new IllegalArgumentException("메뉴 가격은 제품 가격의 합을 초과할 수 없습니다");
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
