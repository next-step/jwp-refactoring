package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

    private String name;

    private Integer price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    private MenuRequest(String name, Integer price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, Integer price, Long menuGroupId,
                                 List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public Menu toEntity() {
        return Menu.of(name, price, menuGroupId, menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public String toString() {
        return "MenuRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
