package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;

import java.util.List;
import java.util.Objects;

public class MenuResponse {
    private Long id;
    private String name;
    private Integer price;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(long id, String name, int price, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
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

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().intValue(),
                MenuProductResponse.of(menu.getMenuProducts())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuResponse that = (MenuResponse) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(price, that.price)) return false;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (menuProducts != null ? menuProducts.hashCode() : 0);
        return result;
    }
}
