package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public long calculateSumPrice(List<Long> menuProducts) {
        return products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(findQuantity(menuProducts, product))))
                .mapToLong(productPrices -> productPrices.longValue())
                .sum();
    }

    private Long findQuantity(List<Long> menuProductIds, Product product) {
        return menuProductIds.stream()
                .filter(productId -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
