package kitchenpos.dto.product;

public class ProductRequest {
    private String name;
    private long price;

    public ProductRequest() {}

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
