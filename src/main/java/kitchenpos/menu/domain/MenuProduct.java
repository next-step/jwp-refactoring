package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Quantity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity.getQuantity()));
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
        return quantity.getQuantity();
    }
}
