package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.Column;
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
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    private Quantity quantity;

    protected MenuProduct() {
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    private MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    private MenuProduct(Long id, Menu menu, Product product, Quantity quantity) {
        this(product, quantity);
        this.id = id;
        this.menu = menu;
    }

    public static MenuProduct of(Product product, Quantity quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct of(Long id, Menu menu, Product product, Quantity quantity) {
        return new MenuProduct(id, menu, product, quantity);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal price = product.getPrice();
        BigDecimal bigDecimal = BigDecimal.valueOf(quantity.getQuantity());
        return price.multiply(bigDecimal);
    }
}
