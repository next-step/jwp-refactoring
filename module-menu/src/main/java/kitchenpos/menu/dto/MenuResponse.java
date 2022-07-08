package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    protected MenuResponse() {
    }

    private MenuResponse(Long id, String name, int price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu savedMenu) {
        List<MenuProductResponse> menuProductResponses = savedMenu.getMenuProducts().stream()
                .map(menuProduct -> MenuProductResponse.from(menuProduct))
                .collect(Collectors.toList());
        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                savedMenu.getMenuGroupId(), menuProductResponses);
    }

    public static List<MenuResponse> asListFrom(List<Menu> menus) {
        return menus.stream()
                .map(menu -> MenuResponse.from(menu))
                .collect(Collectors.toList());
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuResponse that = (MenuResponse) o;
        return price == that.price
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(menuGroupId, that.menuGroupId)
                && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
