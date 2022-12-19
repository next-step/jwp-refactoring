package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @JsonProperty(value = "menuProducts")
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public Menu toMenu(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu.Builder()
                .name(name)
                .price(price)
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();
    }

    public static class Builder {

        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProductRequests;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProductRequests(List<MenuProductRequest> menuProductRequests) {
            this.menuProductRequests = menuProductRequests;
            return this;
        }

        public MenuRequest build() {
            return new MenuRequest(name, price, menuGroupId, menuProductRequests);
        }

    }
}
