package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dto.MenuGroupResponse;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> menuProductResponses = new ArrayList<>();
    private MenuGroupResponse menuGroupResponse;

    protected MenuResponse() {
    }

    private MenuResponse(Long id, String name, BigDecimal price, List<MenuProductResponse> menuProductResponses, MenuGroupResponse menuGroupResponse) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProductResponses = menuProductResponses;
        this.menuGroupResponse = menuGroupResponse;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menuProductResponses, MenuGroupResponse.of(menu.getMenuGroup()));
    }

    public static MenuResponse of(Long id, String name, BigDecimal price,List<MenuProductResponse> menuProductResponses , MenuGroupResponse menuGroupResponse) {
        return new MenuResponse(id, name, price, menuProductResponses, menuGroupResponse);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }
}
