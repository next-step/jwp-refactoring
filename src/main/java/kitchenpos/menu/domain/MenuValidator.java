package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        MenuProducts menuProducts = menu.getMenuProducts();
        Products products = new Products(productRepository.findAllById(menuProducts.getProductIds()));
        BigDecimal totalPrice = calculateTotalPrice(menuProducts, products);
        if (menu.getPrice().isBiggerThan(totalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateTotalPrice(MenuProducts menuProducts, Products products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            Product product = products.findById(menuProduct.getProductId());
            sum = sum.add(calculatePrice(menuProduct, product));
        }
        return sum;
    }

    private BigDecimal calculatePrice(MenuProduct menuProduct, Product product) {
        long quantity = menuProduct.getQuantity();
        return product.getPrice().calculatePrice(quantity);
    }
}
