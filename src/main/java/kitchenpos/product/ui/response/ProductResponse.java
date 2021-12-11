package kitchenpos.product.ui.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public final class ProductResponse {

    private long id;
    private String name;
    private BigDecimal price;

    private ProductResponse() {
    }

    private ProductResponse(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }

    public static List<ProductResponse> listFrom(List<Product> products) {
        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
