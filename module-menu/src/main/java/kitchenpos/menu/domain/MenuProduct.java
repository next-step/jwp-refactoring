package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

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
    @Column(nullable = false)
    private Long productId;
    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, new Quantity(quantity));
    }

    public MenuProduct(Long seq, Long productId, long quantity) {
        this(seq, null, productId, new Quantity(quantity));
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        if (this.menu == menu) {
            return;
        }
        this.menu = menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getSeq(), that.getSeq()) && Objects.equals(getMenu(), that.getMenu()) && Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getMenu(), getProductId(), getQuantity());
    }
}
