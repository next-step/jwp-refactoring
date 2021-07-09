package kitchenpos.menu.dto;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dto.MenuGroupResponse;

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

    public static MenuResponse of(Menu menu, List<MenuProductResponse> menuProductResponses) {
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.of(menu.getMenuGroup());
        final String name = menu.getName();
        return new MenuResponse(menu.id(), name, menu.priceToInt(), menuGroupResponse, menuProductResponses);
    }

//    public static List<MenuResponse> listOf(List<Menu> menus) {
//        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
//    }

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
