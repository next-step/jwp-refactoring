package kitchenpos.product.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final Integer price;

    public ProductResponse(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getPrice());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
