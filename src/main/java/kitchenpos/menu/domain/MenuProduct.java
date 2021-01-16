package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this.product = Objects.requireNonNull(product);
        this.quantity = new Quantity(quantity);
    }

    public void assign(final Menu menu) {
        this.menu = menu;
    }

    public Money price() {
        Money productPrice = product.getPrice();
        return productPrice.multiply(quantity.getQuantity());
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

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
