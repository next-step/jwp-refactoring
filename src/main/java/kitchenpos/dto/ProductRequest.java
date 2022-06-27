package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductRequest {
    private String name;
    private long price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }
}
