package kitchenpos.menu.dto;

import kitchenpos.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private final long id;
    private final String name;
    private final int price;
    private final String groupName;
    private final List<MenuProductResponse> menuProducts;


    public MenuResponse(long id, String name, int price, String groupName, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.groupName = groupName;
        this.menuProducts = menuProducts;
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

    public String getGroupName() {
        return groupName;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static MenuResponse ofMenu(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .stream()
                .map(MenuProductResponse::ofProduct).collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().intValue(), menu.getMenuGroup().getName(), menuProductResponses);
    }
}
