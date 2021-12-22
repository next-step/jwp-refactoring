package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Amount;

public class MenuResponse {

    private Long id;
    private String name;
    private Amount price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public static MenuResponse of(Menu savedMenu) {
        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
            savedMenu.getMenuGroup().getId(), savedMenu.getMenuProducts());
    }

    public MenuResponse(Long id, String name, Amount price, Long menuGroupId,
        MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProductResponse.ofList(menuProducts);
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

    public Amount getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
