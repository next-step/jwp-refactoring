package kitchenpos.menu.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(){}

    private MenuProduct(MenuProductBuilder builder) {
        this.seq = builder.seq;
        this.menuId = builder.menuId;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }
    public static MenuProductBuilder builder(){
        return new MenuProductBuilder();
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
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

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
