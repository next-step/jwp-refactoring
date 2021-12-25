package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private long price;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductResponse(Long id) {
        this.id = id;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
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

    public long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
