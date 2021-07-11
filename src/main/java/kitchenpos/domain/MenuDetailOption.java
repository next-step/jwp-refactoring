package kitchenpos.domain;

public class MenuDetailOption {
    private String name;
    private Price price;
    private Quantity quantity;

    public MenuDetailOption(String name, Price price, Quantity quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
