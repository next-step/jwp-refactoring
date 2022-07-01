package kitchenpos.product.domain;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Products {
    private final Map<Long, Product> products;

    public Products(List<Product> products) {
        this.products = products.stream()
                .collect(collectingAndThen(toMap(Product::getId, Function.identity()), Collections::unmodifiableMap));
    }

    public Product getProduct(Long productId) {
        if (!products.containsKey(productId)) {
            throw new IllegalStateException("[ERROR] productId 대한 값이 없습니다.");
        }
        return products.get(productId);
    }

}
