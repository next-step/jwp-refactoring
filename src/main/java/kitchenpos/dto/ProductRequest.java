package kitchenpos.dto;

import java.math.BigInteger;

public class ProductRequest {
    private String name;
    private BigInteger price;

    public ProductRequest(String name, BigInteger price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }
}
