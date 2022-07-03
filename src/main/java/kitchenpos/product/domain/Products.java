package kitchenpos.product.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.dto.ProductResponse;

import java.util.*;
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

    public Product findMenuById(Long id) {
        return this.value.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean isNotAllContainIds(Collection<Long> ids) {
        Set<Long> removeDuplicatedIds = new HashSet<>(ids);

        return this.value.size() != removeDuplicatedIds.size() ||
                this.value.stream().map(Product::getId).anyMatch(id -> !removeDuplicatedIds.contains(id));
    }
}
