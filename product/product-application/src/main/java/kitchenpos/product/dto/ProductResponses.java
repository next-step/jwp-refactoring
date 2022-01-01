package kitchenpos.product.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.product.domain.Product;

public class ProductResponses {
    private final List<ProductResponse> productResponses;

    private ProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public static ProductResponses from(List<Product> products) {
        return new ProductResponses(products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList()));
    }

    public List<ProductResponse> getProductResponses() {
        return Collections.unmodifiableList(productResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductResponses))
            return false;
        ProductResponses that = (ProductResponses)o;
        return Objects.equals(productResponses, that.productResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productResponses);
    }
}
