package kitchenpos.helper;

import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuRequestBuilder {
    private Long id;
    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    private MenuRequestBuilder() {
    }

    public static MenuRequestBuilder builder() {
        return new MenuRequestBuilder();
    }

    public MenuRequestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MenuRequestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuRequestBuilder price(Integer price) {
        this.price = price;
        return this;
    }

    public MenuRequest build() {
        return new MenuRequest(id, name, price, menuGroupId, menuProductRequests);
    }
}
