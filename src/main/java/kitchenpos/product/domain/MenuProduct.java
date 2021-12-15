package kitchenpos.product.domain;

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
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import org.springframework.util.Assert;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @JoinColumn(name = "menu_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    @ManyToOne(optional = false)
    private Menu menu;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, Quantity quantity) {
        Assert.notNull(product, "상품은 필수입니다.");
        Assert.notNull(quantity, "수량은 필수입니다.");
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, Quantity quantity) {
        return new MenuProduct(product, quantity);
    }

    public long seq() {
        return seq;
    }

    public Product product() {
        return product;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Price price() {
        return quantity.multiply(product.price());
    }

    void changeMenu(Menu menu) {
        Assert.notNull(menu, "변경하려는 메뉴는 필수입니다.");
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, product, quantity);
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
        return Objects.equals(seq, that.seq) && Objects
            .equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
            "seq=" + seq +
            ", product=" + product +
            ", quantity=" + quantity +
            '}';
    }
}
