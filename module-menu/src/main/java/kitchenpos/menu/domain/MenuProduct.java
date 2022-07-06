package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    MenuProduct(Builder builder) {
        this.seq = builder.seq;
        this.menu = builder.menu;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public Price price(Price price) {
        return price.multiply(this.quantity.value());
    }

    public Quantity quantity() {
        return this.quantity;
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
                && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, productId, quantity);
    }

    public boolean sameProductId(Long productId) {
        return this.productId.equals(productId);
    }

    public static class Builder {
        private Long seq;
        private Menu menu;
        private Long productId;
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

        public Builder setProductId(Long productId) {
            this.productId = productId;
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
