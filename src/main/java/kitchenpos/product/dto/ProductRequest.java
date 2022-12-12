package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private Long money;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long money) {
        this.name = name;
        this.money = money;
    }

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(ProductPrice.of(money))
                .build();
    }

    public String getName() {
        return name;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public void setName(String name) {
        this.name = name;
    }
}
