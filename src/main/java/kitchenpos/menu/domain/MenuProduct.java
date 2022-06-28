package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Quantity;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;
    @Column(nullable = false)
    private Long productId;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct from(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public Long seq() {
        return seq;
    }

    public Long menuId() {
        return menu.id();
    }

    public void addMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long productId() {
        return productId;
    }

    public Quantity quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
