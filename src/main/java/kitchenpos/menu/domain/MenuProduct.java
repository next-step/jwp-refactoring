package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.exception.InvalidQuantityException;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    private static final Long MIN_QUANTITY = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    MenuProduct(Builder builder) {
        this.seq = builder.seq;
        this.menu = builder.menu;
        this.product = builder.product;
        this.quantity = builder.quantity;
    }

    public Price price() {
        return this.product.multiply(quantity.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq) && Objects.equals(menu, that.menu)
                && Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, product, quantity);
    }

    public static class Builder {
        private Long seq;
        private Menu menu;
        private Product product;
        private Quantity quantity;

        public Builder(Menu menu) {
            this.menu = menu;
        }

        public Builder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder setMenu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setQuantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }
}
