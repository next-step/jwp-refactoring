package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Price;

public class MenuSimpleRequest {
    private final String name;
    private final Price price;

    public MenuSimpleRequest(String name, Price price) {
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
