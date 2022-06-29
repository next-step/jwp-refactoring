package kitchenpos.product.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    protected ProductRequest() {
    }

    public ProductRequest(final String name, final long price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "name='" + name + '\'' +
                ", unitPrice=" + price +
                '}';
    }
}
