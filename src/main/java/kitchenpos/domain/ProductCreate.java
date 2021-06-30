package kitchenpos.domain;

public class ProductCreate {
    private String name;
    private Price price;

    public ProductCreate(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
