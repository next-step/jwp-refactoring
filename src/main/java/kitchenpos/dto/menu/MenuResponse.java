package kitchenpos.dto.menu;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.ProductResponse;

import static java.util.stream.Collectors.*;

import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private int price;
    private MenuGroupResponse menuGroupResponse;
    private List<ProductResponse> productResponses;

    public MenuResponse(Long id, String name, Price price, MenuGroupResponse menuGroupResponse, List<ProductResponse> productResponses) {
        this.id = id;
        this.name = name;
        this.price = price.getPrice();
        this.menuGroupResponse = menuGroupResponse;
        this.productResponses = productResponses;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.of(menu.getMenuGroup()), ProductResponse.ofList(menu.getMenuProducts()));
    }

    public static List<MenuResponse> ofList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(toList());
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

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}
