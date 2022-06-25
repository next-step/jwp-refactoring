package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct from(Product product, long quantity) {
        return new MenuProduct(product, quantity);
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

    public Price productPrice() {
        return product.price();
    }

    public Long productId() {
        return product.id();
    }

    public Quantity quantity() {
        return quantity;
    }

    public long quantityValue() {
        return quantity.value();
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
