package kitchenpos.dto;

import kitchenpos.domain.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    protected ProductResponse() {
    }

    public static ProductResponse of(ProductEntity product) {
        return new ProductResponse(product.getId(),
                                   product.getName().getValue(),
                                   product.getPrice().getValue());
    }

    public static List<ProductResponse> of(List<ProductEntity> products) {
        return products.stream()
                       .map(ProductResponse::of)
                       .collect(Collectors.toList());
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
