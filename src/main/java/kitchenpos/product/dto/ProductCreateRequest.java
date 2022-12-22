package kitchenpos.product.dto;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(1L, new Name(this.name), new Price(this.price));
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
