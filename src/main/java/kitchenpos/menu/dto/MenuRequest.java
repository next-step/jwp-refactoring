package kitchenpos.menu.dto;

import kitchenpos.common.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public Menu toMenu(final MenuGroup menuGroup, final MenuProducts menuProducts) {
        return new Menu(name,
                new Money(price),
                menuGroup,
                menuProducts);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductRequest {

        private Long productId;
        private long quantity;

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
