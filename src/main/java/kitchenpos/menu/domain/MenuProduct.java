package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity = new Quantity();

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this(null, menu, productId, new Quantity(quantity));
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, new Quantity(quantity));
    }

    public void withMenu(Menu menu) {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MenuProduct that = (MenuProduct) object;
        return Objects.equals(seq, that.seq) &&
                Objects.equals(menu, that.menu) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, productId, quantity);
    }
}
