package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price,
        MenuGroupResponse menuGroupResponse,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProducts = menuProducts;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        List<MenuProductResponse> menuProducts = menu.getMenuProducts().stream()
            .map(MenuProductResponse::new)
            .collect(Collectors.toList());

        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            MenuGroupResponse.from(menu.getMenuGroup()),
            menuProducts
        );
    }
}
