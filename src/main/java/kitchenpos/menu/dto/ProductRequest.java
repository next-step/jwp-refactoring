package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.dto
 * fileName : ProductRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class ProductRequest {
    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
