package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Menu menu;
    @ManyToOne
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this(menu, product, quantity);
        this.seq = seq;
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this(product, quantity);
        this.menu = menu;
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return product.getPrice() * quantity;
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
