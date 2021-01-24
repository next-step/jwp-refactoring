package kitchenpos.menu.util;

import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;

import java.util.ArrayList;
import java.util.List;

public class MenuResponseBuilder {
    private long id;
    private String name;
    private int price;
    private String groupName;
    private final List<MenuProductResponse> menuProductResponses = new ArrayList<>();

    public MenuResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MenuResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MenuResponseBuilder withPrice(int price) {
        this.price = price;
        return this;
    }

    public MenuResponseBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public MenuResponseBuilder addMenuProduct(long productId, String name, int quantity) {
        menuProductResponses.add(new MenuProductResponse(productId, name, quantity));
        return this;
    }

    public MenuResponse build() {
        return new MenuResponse(id, name, price, groupName, menuProductResponses);
    }
}
