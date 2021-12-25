package kitchenpos.mapper;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> toProductResponses(final List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
