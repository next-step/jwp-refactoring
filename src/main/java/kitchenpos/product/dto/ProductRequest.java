package kitchenpos.product.dto;

import kitchenpos.product.domain.Money;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private Long amount;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .money(Money.of(amount))
                .build();
    }

    public String getName() {
        return name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }
}
