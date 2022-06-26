package kitchenpos.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;

    protected ProductResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
