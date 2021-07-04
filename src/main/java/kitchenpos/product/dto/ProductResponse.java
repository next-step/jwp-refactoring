package kitchenpos.product.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;

    public ProductResponse() {}

    private ProductResponse(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Long id, String name, int amount) {
        return new ProductResponse(id, name, amount);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
