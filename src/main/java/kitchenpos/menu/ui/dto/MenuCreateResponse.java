package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateResponse> menuProducts;

    public MenuCreateResponse() {
    }

    public MenuCreateResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = menu.getMenuProducts()
                .stream()
                .map(MenuProductCreateResponse::new)
                .collect(Collectors.toList())
        ;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateResponse> getMenuProducts() {
        return menuProducts;
    }
}
