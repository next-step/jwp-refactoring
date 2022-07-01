package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Products;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final MenuGroupResponse menuGroup;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id,
                        String name,
                        Integer price,
                        MenuGroupResponse menuGroupResponse,
                        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroupResponse;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu, Products products) {
        MenuProducts menuProducts = menu.getMenuProducts();
        List<MenuProductResponse> menuProductResponses = menuProducts.getMenuProducts().stream()
                .map(menuProduct -> MenuProductResponse.of(menuProduct, products))
                .collect(Collectors.toList());

        if (menu.getMenuGroup() != null) {
            return new MenuResponse(menu.getId(),
                    menu.getName(),
                    menu.getPrice().getPrice(),
                    MenuGroupResponse.from(menu.getMenuGroup()),
                    menuProductResponses);
        }

        return new MenuResponse(menu.getId(),
                menu.getName(),
                menu.getPrice().getPrice(),
                null,
                menuProductResponses);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
