package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal money;

    private ProductResponse(Long id,String name,BigDecimal money){
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(),product.getName(),product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
    }
}
