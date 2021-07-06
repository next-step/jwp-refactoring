package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(Product product) {
        return new ProductRequest(product.getName(), product.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
            "name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
