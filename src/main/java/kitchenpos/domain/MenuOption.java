package kitchenpos.domain;

public class MenuOption {
    private String name;
    private Price price;

    public MenuOption(String name, Price price) {
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
