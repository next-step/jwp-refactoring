package kitchenpos.dto;

public class ProductRequest {
    private String name;
    private Long price;

    protected ProductRequest() {
    }

    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "name='" + name + '\'' +
                ", unitPrice=" + price +
                '}';
    }
}
