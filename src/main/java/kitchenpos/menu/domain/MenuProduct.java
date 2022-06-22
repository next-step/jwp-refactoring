package kitchenpos.menu.domain;

import kitchenpos.core.domain.Amount;
import kitchenpos.core.domain.Quantity;
import kitchenpos.product.domain.Product;
import javax.persistence.*;
import static java.util.Objects.requireNonNull;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Embedded
    private Quantity quantity;

    public MenuProduct(Product product, long quantity) {
        this.product = requireNonNull(product, "product");
        this.quantity = new Quantity(quantity);
    }

    protected MenuProduct() {
    }

    public void bindTo(Menu menu) {
        this.menu = menu;
    }

    public Amount calculateAmount() {
        return product.getPrice().multiply(quantity);
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

    public Quantity getQuantity() {
        return quantity;
    }
}
