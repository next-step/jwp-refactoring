package kitchenpos.dto.response;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuViewResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductViewResponse> menuProducts;

    public static MenuViewResponse of(Menu menu, List<MenuProduct> menuProducts) {
        List<MenuProductViewResponse> productViewResponses = filterMenuProductBy(menu, menuProducts)
                .stream()
                .map(MenuProductViewResponse::of)
                .collect(Collectors.toList());

        return new MenuViewResponse(
                menu.getId(),
                menu.getName().toString(),
                menu.getPrice().toBigDecimal(),
                menu.getMenuGroup().getId(),
                productViewResponses
        );
    }

    private static List<MenuProduct> filterMenuProductBy(Menu menu, List<MenuProduct> menuProducts) {
        List<MenuProduct> filteredMenuProducts = menuProducts.stream()
                .filter(item -> item.isSameMenu(menu))
                .collect(Collectors.toList());

        return filteredMenuProducts;
    }

    public MenuViewResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductViewResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductViewResponse> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuViewResponse that = (MenuViewResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
