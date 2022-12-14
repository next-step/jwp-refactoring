package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @Column
    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(MenuProductBuilder builder) {
        this.seq = builder.seq;
        this.menu = builder.menu;
        this.product = builder.product;
        this.quantity = builder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        if (Objects.isNull(product)) {
            return null;
        }
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Menu menu;
        private Product product;
        private long quantity;

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public MenuProductBuilder product(Product product) {
            this.product = product;
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

}
