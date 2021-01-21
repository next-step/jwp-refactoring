package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public static MenuResponse of(Menu menu) {
        MenuGroup menuGroup = menu.getMenuGroup();
        if (menuGroup == null) {
            throw new IllegalArgumentException();
        }

        return new MenuResponse(
                menu.getId(), menu.getName(), menu.getPrice(), menuGroup.getId(),
                menu.getMenuProducts().stream()
                        .map(MenuProductResponse::of)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuResponse(Long id, String name, BigDecimal price) {
        this(id, name, price, null, Collections.emptyList());
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, price, menuGroupId, Collections.emptyList());
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }
}
