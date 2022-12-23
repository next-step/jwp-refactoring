package kitchenpos.menu.dto;

import java.math.BigDecimal;
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

    public static Product to(ProductRequest productRequest){
        return new Product(productRequest.getName(), productRequest.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
