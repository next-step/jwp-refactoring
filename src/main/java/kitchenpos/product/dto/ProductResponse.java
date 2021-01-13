package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse() {
    }

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static List<ProductResponse> ofList(final List<Product> products) {
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getPrice()
                        .getAmount());
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
