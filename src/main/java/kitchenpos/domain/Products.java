package kitchenpos.domain;

import kitchenpos.application.EntityNotExistsException;

import java.util.List;

public class Products {
    private List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public int size() {
        return products.size();
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(EntityNotExistsException::new);
    }
}
