package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.product.domain.Price;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse,
                        List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of (Menu menu, MenuProducts menuProducts) {
        Price price = menu.getPrice();
        return new MenuResponse(menu.getId(), menu.getName(), price.getValue(),
                MenuGroupResponse.of(menu.getMenuGroup()),
                menuProductsToMenuProductResponses(menuProducts));
    }

    private static List<MenuProductResponse> menuProductsToMenuProductResponses(MenuProducts menuProducts) {
        return menuProducts.getValue()
                .stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
