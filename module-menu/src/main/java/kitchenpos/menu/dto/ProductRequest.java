package kitchenpos.menu.dto;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Product;

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
        return new ProductRequest(product.getName(), product.getPrice().getAmount());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, Price.wonOf(price));
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
            "name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
