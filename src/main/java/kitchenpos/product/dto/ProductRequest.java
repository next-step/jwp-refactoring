package kitchenpos.product.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
    @NotNull(message = "상품 이름은 필수값입니다.")
    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

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
}
