package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.id(), product.name(), product.price());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
