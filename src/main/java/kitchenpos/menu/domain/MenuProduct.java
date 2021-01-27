package kitchenpos.menu.domain;

import kitchenpos.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "productId")
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return getProduct().getPrice().multiply(BigDecimal.valueOf(getQuantity()));
    }
}
