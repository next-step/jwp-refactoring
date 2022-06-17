package kitchenpos.domain;

import javax.persistence.*;
import static java.util.Objects.requireNonNull;

@Entity(name = "menu_product")
public class MenuProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
    @Embedded
    private Quantity quantity;

    public MenuProductEntity(ProductEntity product, long quantity) {
        this.product = requireNonNull(product, "product");
        this.quantity = new Quantity(quantity);
    }

    protected MenuProductEntity() {
    }

    public void bindTo(MenuEntity menu) {
        this.menu = menu;
    }

    public Amount calculateAmount() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
