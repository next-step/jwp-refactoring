package kitchenposNew.menu.dto;

import kitchenposNew.menu.domain.Product;
import kitchenposNew.wrap.Price;

import java.math.BigDecimal;

public class ProductRequest {
    public Long id;
    public String name;
    public BigDecimal price;

    protected ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price){
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, new Price(price));
    }
}
