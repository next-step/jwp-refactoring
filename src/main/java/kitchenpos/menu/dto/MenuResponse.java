package kitchenpos.menu.dto;

import java.util.List;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menuproduct.dto.MenuProductResponse;

public class MenuResponse {
    private Long id;
    private String name;
    private int price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse() {}

    private MenuResponse(Long id, String name, int price, MenuGroupResponse menuGroupListResponse, List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupListResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Long id, String name, int amount, MenuGroupResponse menuGroupResponse, List<MenuProductResponse> menuProductResponses) {
        return new MenuResponse(id, name, amount, menuGroupResponse, menuProductResponses);
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
