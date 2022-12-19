package kitchenpos.menu.domain;

import kitchenpos.core.domain.Quantity;
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

    @JoinColumn(name = "product_id", nullable = false,  foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    private Long productId;

    @Embedded
    private Quantity quantity;

    public MenuProduct(Long productId, long quantity) {
        this.productId = requireNonNull(productId, "product");
        this.quantity = new Quantity(quantity);
    }

    protected MenuProduct() {
    }

    public void bindTo(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
