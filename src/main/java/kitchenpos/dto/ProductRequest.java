package kitchenpos.dto;

import javax.validation.constraints.Positive;

public class ProductRequest {
    private String name;
    @Positive
    private long price;

    protected ProductRequest(){}

    public ProductRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
