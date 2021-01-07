package kitchenpos.ui.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    MenuRequest() {
    }

    MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(final String name, final BigDecimal price, final Long menuGroupId,
                          final List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
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
        return menuProductRequests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProductRequests, that.menuProductRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProductRequests);
    }

    @Override
    public String toString() {
        return "MenuRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProductRequests=" + menuProductRequests +
                '}';
    }
}
