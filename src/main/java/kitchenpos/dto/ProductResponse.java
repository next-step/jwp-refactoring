package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    protected ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue()
        );
    }

    public static List<ProductResponse> of(List<Product> products) {
        return products.stream().
                map(ProductResponse::of).
                collect(Collectors.toList());
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
