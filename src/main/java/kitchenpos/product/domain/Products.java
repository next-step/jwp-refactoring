package kitchenpos.product.domain;

import java.util.List;

public class Products {
    List<Product> elements;

    public Products(List<Product> elements) {
        this.elements = elements;
    }

    public Product findById(Long id) {
        return elements.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
