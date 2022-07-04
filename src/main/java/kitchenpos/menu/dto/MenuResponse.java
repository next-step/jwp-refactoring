package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long menuId;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public MenuResponse() {
    }

    public MenuResponse(final Menu menu) {
        menuId = menu.getId();
        name = menu.getName();
        price = menu.getPrice();
        menuGroupId = menu.getMenuGroupId();
        if (Objects.nonNull(menu.getMenuProducts())) {
            menuProductInfos = menu.getMenuProducts().stream()
                    .map(MenuProductInfo::new)
                    .collect(Collectors.toList());
        }
    }

    private List<MenuProductInfo> menuProductInfos;

    public Long getMenuId() {
        return menuId;
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

    public List<MenuProductInfo> getMenuProductInfos() {
        return menuProductInfos;
    }

    public static class MenuProductInfo {
        private Long productId;
        private long quantity;

        public MenuProductInfo() {
        }

        public MenuProductInfo(MenuProduct menuProduct) {
            this.productId = menuProduct.getProduct().getId();
            this.quantity = menuProduct.getQuantity();
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
