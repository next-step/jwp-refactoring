package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu() {
        Menu menu = new Menu(name, price, menuGroupId);
        menu.addMenuProducts(menuProducts);
        return menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuRequest that = (MenuRequest) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getMenuGroupId(), that.getMenuGroupId()) && Objects.equals(getMenuProducts(), that.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}
