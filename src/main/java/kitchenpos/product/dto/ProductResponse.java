package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static List<ProductResponse> fromList(List<Product> products) {
        return products.stream().map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPriceVal());
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(),
            that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
