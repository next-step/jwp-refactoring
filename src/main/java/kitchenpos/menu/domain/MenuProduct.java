package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    public MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(Product product, Quantity quantity) {
        this(null, null, product, quantity);
    }

    public void associateMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price getTotalPrice() {
        return this.product.getPrice()
                .multiplyByQuantity(this.quantity);
    }
}
