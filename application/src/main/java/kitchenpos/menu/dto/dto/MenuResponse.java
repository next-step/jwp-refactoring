package kitchenpos.menu.dto.dto;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.dto.MenuDto;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() { }

    public MenuResponse(String name, Long price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public MenuResponse(Long id, String name, Long price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(MenuDto menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                                                             .stream()
                                                             .map(MenuProductResponse::of)
                                                             .collect(toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getId(),
                                menuProductResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuResponse menuResponse = (MenuResponse) o;
        return Objects.equals(id, menuResponse.id) && Objects.equals(name, menuResponse.name)
            && Objects.equals(price, menuResponse.price) && Objects.equals(menuGroupId,
                                                                           menuResponse.menuGroupId)
            && Objects.equals(menuProducts, menuResponse.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
