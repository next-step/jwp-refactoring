package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(long quantity) {
        this.quantity = quantity;
    }

    public MenuProduct(Product product, long quantity) {
        this(quantity);
        this.product = product;
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this(product, quantity);
        this.menu = menu;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal sumProductPrice() {
        return product.multiplyPrice(BigDecimal.valueOf(this.quantity));
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
}
