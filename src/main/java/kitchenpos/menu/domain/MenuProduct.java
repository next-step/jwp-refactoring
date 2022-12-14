package kitchenpos.menu.domain;

import kitchenpos.price.domain.Amount;
import kitchenpos.price.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this.product = requireNonNull(product, "product");
        this.quantity = quantity;
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
