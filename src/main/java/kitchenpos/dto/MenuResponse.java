package kitchenpos.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> products;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroup = new MenuGroupResponse(menu.getMenuGroup());
        this.products = menu.getMenuProducts().stream().map(MenuProductResponse::new).collect(Collectors.toList());
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getProducts() {
        return products;
    }
}
