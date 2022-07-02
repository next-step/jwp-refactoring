package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;

    protected ProductResponse() {
    }

    private ProductResponse(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> asListFrom(List<Product> products) {
        return products.stream()
                .map(product -> ProductResponse.from(product))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return getPrice() == that.getPrice()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice());
    }
}
