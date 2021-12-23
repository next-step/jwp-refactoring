package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private MenuProductResponses menuProductResponses;

    protected MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, MenuProductResponses menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                MenuProductResponses.of(menuProducts)
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses.getMenuProductResponses();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && price.compareTo(that.price) == 0
                && Objects.equals(menuGroupId, that.menuGroupId)
                && Objects.equals(menuProductResponses, that.menuProductResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProductResponses);
    }
}
