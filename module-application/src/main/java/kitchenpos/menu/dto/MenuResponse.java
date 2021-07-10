package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.Menu;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> productResponses = menu.getMenuProducts()
            .stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());

        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroupId(),
            productResponses);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
