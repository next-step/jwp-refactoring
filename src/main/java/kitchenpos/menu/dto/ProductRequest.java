package kitchenpos.menu.dto;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    private ProductRequest() {}

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product.Builder()
                .name(name)
                .price(price)
                .build();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static class Builder {

        private String name;
        private BigDecimal price;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductRequest build() {
            return new ProductRequest(name, price);
        }
    }
}
