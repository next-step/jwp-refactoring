package kitchenpos.menu.validator;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        Price totalProductsPrice = calculateTotalProductsPrice(menu.getMenuProducts());
        if (menu.price().isGreaterThan(totalProductsPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품들의 가격 합보다 크면 안된다");
        }

    }

    private Price calculateTotalProductsPrice(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts()
            .stream()
            .map(this::menuProductPrice)
            .reduce(Price.ZERO, Price::add);
    }

    private Price menuProductPrice(MenuProduct menuProduct) {
        Price productPrice = getPrductPrice(menuProduct.productId());
        return menuProduct.calculateTotalPrice(productPrice);
    }

    private Price getPrductPrice(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        return product.getPrice();
    }
}
