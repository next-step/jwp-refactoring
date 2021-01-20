package kitchenpos.dto;

import kitchenpos.domain.menu.Product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank
    private String name;
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    protected ProductRequest() {}

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
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

    public static ProductRequestBuilder builder() {
        return new ProductRequestBuilder();
    }

    public static final class ProductRequestBuilder {
        private String name;
        private BigDecimal price;

        private ProductRequestBuilder() {}

        public ProductRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductRequestBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductRequest build() {
            return new ProductRequest(name, price);
        }
    }
}
