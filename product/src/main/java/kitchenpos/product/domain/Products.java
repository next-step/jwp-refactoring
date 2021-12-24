package kitchenpos.product.domain;

import java.util.List;

import kitchenpos.product.exception.NotFoundProductException;

public class Products {
    List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products of(List<Product> products) {
        return new Products(products);
    }

    public Product findById(Long productId) {
        return products.stream()
                        .filter(product -> product.isEqualId(productId))
                        .findFirst()
                        .orElseThrow(NotFoundProductException::new);
    }

    public int size() {
        return this.products.size();
    }
}
