package kitchenpos.tobe.menu.domain;

import kitchenpos.tobe.product.domain.Product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long quantity;

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
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

        public Builder product(Product product) {
            menuProduct.product = product;
            return this;
        }

        public Builder quantity(Long quantity) {
            menuProduct.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return menuProduct;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProduct)) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getSeq(), that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq());
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menu=" + menu +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
