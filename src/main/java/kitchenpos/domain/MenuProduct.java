package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void changeMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuProduct that = (MenuProduct)o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menuId,
            that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
