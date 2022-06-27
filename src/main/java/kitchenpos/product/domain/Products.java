package kitchenpos.product.domain;

import kitchenpos.product.dto.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Products {
    private List<Product> value = new ArrayList<>();

    public Products(List<Product> products) {
        this.value.addAll(products);
    }

    public List<ProductResponse> toResponse() {
        return this.value.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public List<Product> getValue() {
        return value;
    }
}
