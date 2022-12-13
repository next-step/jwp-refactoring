package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(ProductPrice.of(price))
                .build();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return price;
    }

    public void setMoney(BigDecimal price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
