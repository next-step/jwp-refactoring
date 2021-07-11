package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal price() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

    public void updateMenu(final Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MenuProduct that = (MenuProduct)o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menu,
            that.menu) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, product, quantity);
    }
}
