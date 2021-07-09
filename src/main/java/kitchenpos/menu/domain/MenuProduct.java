package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne
    private Product product;

    private long quantity;

    protected MenuProduct() { }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void addedBy(Long menuId) {
        this.menuId = menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
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
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects
            .equals(menuId, that.menuId) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, product, quantity);
    }
}
