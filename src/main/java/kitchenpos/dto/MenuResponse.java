package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProduct {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProduct() {
        }

        public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getSeq() {
            return seq;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}