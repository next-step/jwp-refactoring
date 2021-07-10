package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Products {
    private final List<Product> productList;

    public Products(List<Product> productList) {
        this.productList = productList;
    }

    public BigDecimal calculatePrice(Long productId, long quantity) {
        return productList.stream()
            .filter(product -> Objects.equals(product.getId(), productId))
            .findFirst()
            .map(product -> product.getPrice().multiply(new BigDecimal(quantity)))
            .orElseThrow(IllegalArgumentException::new);
    }
}
