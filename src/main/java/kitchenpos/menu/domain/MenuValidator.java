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
        if (menu.getPrice().getValue().compareTo(calculateTotalPrice(menuProducts, products)) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateTotalPrice(MenuProducts menuProducts, Products products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            Product product = products.findById(menuProduct.getProductId());
            sum = sum.add(getMultiply(menuProduct, product));
        }
        return sum;
    }

    private BigDecimal getMultiply(MenuProduct menuProduct, Product product) {
        long quantity = menuProduct.getQuantity();
        BigDecimal price = product.getPrice().getValue();
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
