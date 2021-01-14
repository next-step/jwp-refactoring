package kitchenpos.product.dto;

public class ProductRequest {
    private String name;
    private long price;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ProductRequest setName(final String name) {
        this.name = name;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public ProductRequest setPrice(final long price) {
        this.price = price;
        return this;
    }
}
