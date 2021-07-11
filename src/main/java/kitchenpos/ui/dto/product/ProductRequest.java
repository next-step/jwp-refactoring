package kitchenpos.ui.dto.product;

import java.math.BigDecimal;

public class ProductRequest {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductRequest() {
    }

    private ProductRequest(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, BigDecimal price) {
        return new ProductRequest(null, name, price);
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
