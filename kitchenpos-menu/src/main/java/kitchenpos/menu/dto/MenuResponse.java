package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {}

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProductsReadOnlyValue().stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getNameValue(), menu.getPriceValue(), MenuGroupResponse.from(menu.getMenuGroup()), menuProductResponses);
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
        if (!(o instanceof MenuResponse)) {
            return false;
        }
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(menuGroup, that.menuGroup) && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroupResponse menuGroup;
        private List<MenuProductResponse> menuProducts;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroup(MenuGroupResponse menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(List<MenuProductResponse> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public MenuResponse build() {
            return new MenuResponse(id, name, price, menuGroup, menuProducts);
        }
    }
}
