package kitchenpos.domain.product;

import java.util.List;

import kitchenpos.exception.product.NotFoundProductException;

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
                        .filter(product -> product.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(NotFoundProductException::new);
    }
    
}
