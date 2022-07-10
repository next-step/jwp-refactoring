package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(name = "menu_id")
    private Long menuId;
    private Long productId;
    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = null;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long productId, final long quantity) {
        return new MenuProduct(null, productId, quantity);
    }

    public boolean equalsProductId(final Long productId) {
        return this.productId ==productId;
    }

    public void updateMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
