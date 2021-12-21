package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final List<MenuProductResponse> menuProducts = new ArrayList<>();
    private final Long menuGroupId;


    public MenuResponse(Long id, String name, int price, List<MenuProductResponse> menuProducts, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts.addAll(menuProducts);
        this.menuGroupId = menuGroupId;
    }

    public static MenuResponse of(Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().intValue(), menuProductResponses, menu.getMenuGroupId());
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuResponse that = (MenuResponse) o;

        if (price != that.price) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (menuProducts != null ? !menuProducts.equals(that.menuProducts) : that.menuProducts != null) return false;
        return menuGroupId != null ? menuGroupId.equals(that.menuGroupId) : that.menuGroupId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (menuProducts != null ? menuProducts.hashCode() : 0);
        result = 31 * result + (menuGroupId != null ? menuGroupId.hashCode() : 0);
        return result;
    }
}
