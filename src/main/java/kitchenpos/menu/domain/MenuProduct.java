package kitchenpos.menu.domain;

import kitchenpos.common.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    //@OneToOne(fetch = LAZY)
    @ManyToOne(fetch = LAZY)
    private Product product;
    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(null, null, product, new Quantity(quantity));
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        this(seq, null, product, new Quantity(quantity));
    }

    public MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        if (this.menu == menu) {
            return;
        }
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getSeq(), that.getSeq()) && Objects.equals(getMenu(), that.getMenu()) && Objects.equals(getProduct(), that.getProduct()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getMenu(), getProduct(), getQuantity());
    }
}
