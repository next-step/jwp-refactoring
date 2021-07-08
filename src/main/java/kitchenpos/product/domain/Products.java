package kitchenpos.product.domain;

import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public long calculateSumPrice(List<MenuProduct> menuProducts) {
        return products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(findQuantity(menuProducts, product))))
                .mapToLong(productPrices -> productPrices.longValue())
                .sum();
    }

    private Long findQuantity(List<MenuProduct> menuProducts, Product product) {
        return menuProducts.stream()
                .filter(menuProduct -> product.getId().equals(menuProduct.getProductId()))
                .findFirst()
                .map(menuProduct -> menuProduct.getQuantity())
                .orElseThrow(IllegalArgumentException::new);
    }
}
