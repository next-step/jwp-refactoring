package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        Menu menu = new Menu(name, price, menuGroupId);
        menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .forEach(menu::addMenuProduct);

        return menu;
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

    public static class MenuProductRequest {
        private Long productId;
        private long quantity;

        public MenuProductRequest() {
        }

        public MenuProductRequest(Long productId, long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public MenuProduct toEntity() {
            return new MenuProduct(productId, quantity);
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
