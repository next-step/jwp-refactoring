package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    protected MenuProduct() {
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

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal price = product.getPrice();
        BigDecimal bigDecimal = BigDecimal.valueOf(quantity);
        return price.multiply(bigDecimal);
    }
}
