package kitchenpos.menu.domain;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Long productId;
    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Long product, long quantity) {
        this(null, menu, product, new Quantity(quantity));
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
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

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return getQuantity() == that.getQuantity() && Objects.equals(getSeq(), that.getSeq()) && Objects.equals(getMenu(), that.getMenu()) && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getMenu(), getProductId(), getQuantity());
    }
}
