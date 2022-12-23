package kitchenpos.menu.domain;

import common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public BigDecimal getMenuProductPrice() {
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
