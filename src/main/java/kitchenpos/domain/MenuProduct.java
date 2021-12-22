package kitchenpos.domain;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(builder builder) {
        this.seq = builder.seq;
        this.menuId = builder.menuId;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class builder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public builder() {
        }

        public builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }

}
