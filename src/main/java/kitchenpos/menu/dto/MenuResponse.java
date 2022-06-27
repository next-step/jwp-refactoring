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
        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice().intValue(),
                savedMenu.getMenuGroup().getId(), menuProductResponses);
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
        return getPrice() == that.getPrice()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getMenuGroupId(), that.getMenuGroupId())
                && Objects.equals(getMenuProducts(), that.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}
