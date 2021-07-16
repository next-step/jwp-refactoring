package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
        // empty
    }

    private MenuRequest(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroupId = builder.menuGroupId;
        this.menuProducts = builder.menuProducts;
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

    public Menu toMenu(final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return Menu.Builder.of(this.name, Price.of(this.price))
                           .menuGroup(menuGroupId)
                           .menuProducts(menuProducts)
                           .build();
    }


    public static class Builder {
        private final String name;
        private final BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        private Builder(final String name, final BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public static Builder of(final String name, final BigDecimal price) {
            return new Builder(name, price);
        }

        public Builder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(List<MenuProductRequest> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public MenuRequest build() {
            return new MenuRequest(this);
        }
    }
}
