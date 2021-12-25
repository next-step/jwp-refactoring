package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, Quantity quantity) {
        return new MenuProduct(null, null, product, quantity);
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    public boolean equalsMenu(Menu menu) {
        if (this.menu != null) {
            return this.menu.equals(menu);
        }

        return false;
    }

    public BigDecimal totalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(getQuantity()));
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
        return quantity.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
