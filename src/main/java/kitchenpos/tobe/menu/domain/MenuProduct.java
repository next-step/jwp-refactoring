package kitchenpos.tobe.menu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long productId;

    private long quantity;

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MenuProduct menuProduct = new MenuProduct();

        public Builder id(Long seq) {
            menuProduct.seq = seq;
            return this;
        }

        public Builder menu(Menu menu) {
            menuProduct.menu = menu;
            return this;
        }

        public Builder productId(Long productId) {
            menuProduct.productId = productId;
            return this;
        }

        public Builder quantity(long quantity) {
            menuProduct.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return menuProduct;
        }
    }
}
