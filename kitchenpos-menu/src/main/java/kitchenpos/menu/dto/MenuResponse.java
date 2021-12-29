package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price,
        MenuGroupResponse menuGroup,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static List<MenuResponse> fromList(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuResponse from(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProductList()
            .stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPriceVal(),
            MenuGroupResponse.from(menu.getMenuGroup()), menuProductResponses);
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(),
            that.getName()) && Objects.equals(getMenuGroup(), that.getMenuGroup())
            && Objects.equals(getMenuProducts(), that.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMenuGroup(), getMenuProducts());
    }
}
