package kitchenpos.dto.request;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.ProductCreate;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public ProductCreate toCreate() {
        return new ProductCreate(new Name(name), new Price(price));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
