package kitchenpos.dto;

public class ProductResponse {
    private long id;
    private String name;
    private long price;

    protected ProductResponse(){}

    public ProductResponse(long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
