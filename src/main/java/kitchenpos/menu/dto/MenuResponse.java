package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private long price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, long price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        // TODO Menu에 MenuGroup 생성시 null 제거
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), null, MenuProductResponse.ofList(menu.getMenuProducts()));
    }

    public static List<MenuResponse> ofList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroup, that.menuGroup) && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
